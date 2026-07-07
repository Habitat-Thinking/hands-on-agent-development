package com.russmiles.confplanner.agent;

import com.embabel.agent.api.invocation.AgentInvocation;
import com.embabel.agent.core.Budget;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.test.integration.EmbabelMockitoIntegrationTest;
import com.russmiles.confplanner.domain.AttendeeProfile;
import com.russmiles.confplanner.domain.PersonalSchedule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Proof that the noDoubleBooking invariant actually bites at runtime (Lab 3).
 *
 * <p>We feed the planner a draft that books PC-01 and AI-01 — both at 2026-09-15 09:00 — so the
 * draft always clashes. Because the goal action {@code confirmSchedule} requires noDoubleBooking,
 * a clashing draft never satisfies the goal; the planner re-runs assembly, the deterministic mock
 * returns the same clash, and the run hits the budget and stops without ever producing a schedule.
 */
class GuardrailEnforcementTest extends EmbabelMockitoIntegrationTest {

    @BeforeAll
    static void setUp() {
        System.setProperty("embabel.agent.shell.interactive.enabled", "false");
    }

    @Test
    void aDoubleBookedDraftCanNeverReachTheGoal() {
        var input = new UserInput("schedule me");

        whenCreateObject(prompt -> prompt.contains("extract a structured profile"),
                AttendeeProfile.class)
                .thenReturn(new AttendeeProfile(
                        List.of("kubernetes", "ai"), "Engineer", "Advanced", List.of("learn"), List.of()));

        whenCreateObject(prompt -> prompt.contains("Pick the 8-14 sessions"),
                ConfPlannerAgent.Shortlisting.class)
                .thenReturn(new ConfPlannerAgent.Shortlisting(List.of("PC-01", "AI-01"), "x"));

        whenCreateObject(prompt -> prompt.contains("why it is relevant"),
                ConfPlannerAgent.ResearchOutput.class)
                .thenReturn(new ConfPlannerAgent.ResearchOutput(List.of(
                        new ConfPlannerAgent.Insight("PC-01", "k8s", 0.9),
                        new ConfPlannerAgent.Insight("AI-01", "ai", 0.9))));

        // Both sessions share the 2026-09-15 09:00 slot — an unavoidable double-booking.
        whenCreateObject(prompt -> prompt.contains("Build this attendee a personal schedule"),
                ConfPlannerAgent.ScheduleDraft.class)
                .thenReturn(new ConfPlannerAgent.ScheduleDraft(List.of("PC-01", "AI-01"), "clash"));

        // Small budget so the unsatisfiable goal stops quickly instead of spinning.
        var options = ProcessOptions.DEFAULT.withBudget(new Budget(0.5, 8, 200_000));

        assertThrows(Exception.class, () -> AgentInvocation
                .builder(agentPlatform)
                .options(options)
                .build(PersonalSchedule.class)
                .invoke(input), "a clashing schedule must never satisfy the goal");
    }
}
