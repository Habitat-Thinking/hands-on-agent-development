package com.russmiles.confplanner.agent;

import com.embabel.agent.api.common.Ai;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.test.unit.FakeOperationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.russmiles.confplanner.domain.AttendeeProfile;
import com.russmiles.confplanner.service.CatalogService;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit tests for individual actions, with the LLM faked. No Spring context, no API keys:
 * we hand the action a {@link FakeOperationContext} whose next response we queue ourselves,
 * then assert on what the action did with it.
 */
class ConfPlannerAgentTest {

    private final CatalogService catalogService = new CatalogService(new ObjectMapper());
    private final ConfPlannerAgent agent = new ConfPlannerAgent(catalogService);

    @Test
    void extractAttendeeProfilePutsTheRequestInThePrompt() {
        var context = FakeOperationContext.create();
        context.expectResponse(new AttendeeProfile(
                List.of("kubernetes"), "Platform Engineer", "Advanced",
                List.of("ship faster"), List.of()));

        Ai ai = context.ai();
        agent.extractAttendeeProfile(
                new UserInput("I love Kubernetes and resilience"), ai);

        var prompt = context.getLlmInvocations().getFirst().getMessages().getFirst().getContent();
        assertTrue(prompt.contains("Kubernetes"), "request text should reach the prompt");
        assertTrue(prompt.contains("experienceLevel"), "extraction instructions should be present");
    }

    @Test
    void shortlistResolvesModelChosenIdsToRealSessions() {
        var context = FakeOperationContext.create();
        // The model only emits ids; the action resolves them against the catalog.
        context.expectResponse(new ConfPlannerAgent.Shortlisting(
                List.of("PC-01", "AI-01", "does-not-exist"), "matches interests"));

        var profile = new AttendeeProfile(
                List.of("kubernetes", "ai"), "Engineer", "Intermediate", List.of("learn"), List.of());
        var candidates = agent.shortlistSessions(profile, catalogService.catalog(), context.ai());

        var ids = candidates.sessions().stream().map(s -> s.id()).toList();
        assertEquals(List.of("PC-01", "AI-01"), ids, "unknown ids are dropped, real ones resolved");
    }

    @Test
    void avoidedTopicsAreExcludedFromTheShortlistMenu() {
        var context = FakeOperationContext.create();
        // Even if the model "picks" an avoided session, it was never offered: the menu is filtered.
        context.expectResponse(new ConfPlannerAgent.Shortlisting(List.of("PC-01"), "x"));

        // PC-01 is tagged "kubernetes"; avoiding it must keep it out of the offered menu.
        var profile = new AttendeeProfile(
                List.of("ai"), "Engineer", "Intermediate", List.of("learn"), List.of("kubernetes"));
        agent.shortlistSessions(profile, catalogService.catalog(), context.ai());

        var prompt = context.getLlmInvocations().getFirst().getMessages().getFirst().getContent();
        assertFalse(prompt.contains("PC-01"),
                "sessions tagged with an avoided topic must not appear in the menu");
        // The PromptContributor wiring (avoid-list reaching the prompt) is verified at runtime
        // with `x "..." -p`; FakeOperationContext does not render contributors into the capture.
    }

    @Test
    void noDoubleBookingRejectsTwoSessionsInOneSlot() {
        var sessions = catalogService.catalog().sessions();
        var bySlot = sessions.stream()
                .collect(java.util.stream.Collectors.groupingBy(s -> s.slot()));
        var clashing = bySlot.values().stream().filter(g -> g.size() >= 2).findFirst().orElseThrow();
        var a = clashing.get(0);
        var b = clashing.get(1);

        var clashed = new com.russmiles.confplanner.domain.DraftSchedule(
                List.of(new com.russmiles.confplanner.domain.ScheduleItem(a, a.slot()),
                        new com.russmiles.confplanner.domain.ScheduleItem(b, b.slot())), "clash");
        var clean = new com.russmiles.confplanner.domain.DraftSchedule(
                List.of(new com.russmiles.confplanner.domain.ScheduleItem(a, a.slot())), "ok");

        assertFalse(agent.noDoubleBooking(clashed), "two sessions in one slot must be rejected");
        assertTrue(agent.noDoubleBooking(clean), "a single-session draft is fine");
    }

    @Test
    void hasCandidatesRejectsAnEmptyShortlist() {
        assertFalse(agent.hasCandidates(
                new com.russmiles.confplanner.domain.CandidateSessions(List.of())));
        assertFalse(agent.hasCandidates(null));
    }

    @Test
    void shouldAvoidIsCaseInsensitiveOnTags() {
        var profile = new AttendeeProfile(
                List.of(), "Engineer", "Intermediate", List.of(), List.of("Kubernetes"));
        var k8sSession = catalogService.catalog().sessions().stream()
                .filter(s -> s.tags().contains("kubernetes")).findFirst().orElseThrow();
        assertTrue(profile.shouldAvoid(k8sSession), "avoid match should ignore case");
    }
}
