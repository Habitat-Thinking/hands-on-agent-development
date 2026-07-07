package com.russmiles.confplanner.agent;

import com.embabel.agent.api.invocation.AgentInvocation;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.test.integration.EmbabelMockitoIntegrationTest;
import com.russmiles.confplanner.domain.AttendeeProfile;
import com.russmiles.confplanner.domain.NetworkingPlan;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * The new networking goal reuses the existing pipeline (extract → loadCatalog → shortlist →
 * research) and adds one step, without any change to ConfPlannerAgent. This proves you extend the
 * system by adding a SEPARATE agent that re-declares its upstream steps as thin wrappers over a
 * shared @Service (ConfPlanningCapabilities). An Embabel agent plans only with its OWN actions —
 * nothing plans "through" ConfPlannerAgent's actions. Extend by adding, not rewiring.
 */
class ConfNetworkingAgentIntegrationTest extends EmbabelMockitoIntegrationTest {

    @BeforeAll
    static void setUp() {
        System.setProperty("embabel.agent.shell.interactive.enabled", "false");
    }

    @Test
    void planNetworkingReusesTheSchedulePipeline() {
        var input = new UserInput("I'm into Kubernetes and resilience; who should I meet?");

        whenCreateObject(prompt -> prompt.contains("extract a structured profile"),
                AttendeeProfile.class)
                .thenReturn(new AttendeeProfile(
                        List.of("kubernetes", "resilience"), "Engineer", "Advanced",
                        List.of("meet platform people"), List.of()));

        whenCreateObject(prompt -> prompt.contains("Pick the 8-14 sessions"),
                ConfPlanningCapabilities.Shortlisting.class)
                .thenReturn(new ConfPlanningCapabilities.Shortlisting(List.of("PC-01", "SR-09"), "x"));

        whenCreateObject(prompt -> prompt.contains("why it is relevant"),
                ConfPlanningCapabilities.ResearchOutput.class)
                .thenReturn(new ConfPlanningCapabilities.ResearchOutput(List.of(
                        new ConfPlanningCapabilities.Insight("PC-01", "k8s", 0.9),
                        new ConfPlanningCapabilities.Insight("SR-09", "resilience", 0.9))));

        whenCreateObject(prompt -> prompt.contains("Suggest up to five people"),
                ConfNetworkingAgent.NetworkingDraft.class)
                .thenReturn(new ConfNetworkingAgent.NetworkingDraft(
                        List.of("Dr. Priya Venkatasubramanian", "Marcus Oduya"),
                        "Both speak to your platform and resilience goals."));

        var plan = AgentInvocation
                .create(agentPlatform, NetworkingPlan.class)
                .invoke(input);

        assertNotNull(plan);
        assertFalse(plan.peopleToMeet().isEmpty(), "networking plan should suggest people");
        assertFalse(plan.rationale().isBlank(), "networking plan should explain itself");
    }
}
