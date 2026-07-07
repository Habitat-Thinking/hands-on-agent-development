package com.russmiles.confplanner.mock;

import com.embabel.agent.api.invocation.AgentInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.spi.LlmService;
import com.embabel.common.ai.model.ModelProvider;
import com.russmiles.confplanner.domain.PersonalSchedule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * The key-free proof that the {@code mock} profile actually mocks. Boots the real application
 * context under {@code @ActiveProfiles("mock")} — no API key, no network — and shows that:
 *
 * <ol>
 *   <li>the {@link MockLlmService} bean registers and the {@link ModelProvider} lists a {@code mock}
 *       model (so the profile-gated stub is discovered by
 *       {@code ConfigurableModelProvider}); and</li>
 *   <li>running the ConfPlanner goal end-to-end through that stub yields a real
 *       {@link PersonalSchedule} — every model-backed step served canned JSON in-JVM.</li>
 * </ol>
 *
 * <p>The complementary keyless {@code ./mvnw verify} run (mock profile <em>not</em> active) proves
 * the bean is profile-gated and nothing regresses.
 */
@SpringBootTest(properties = {
        "embabel.agent.shell.interactive.enabled=false",
        "spring.shell.interactive.enabled=false"
})
@ActiveProfiles("mock")
class MockModeIntegrationTest {

    @Autowired
    AgentPlatform agentPlatform;

    @Autowired
    ModelProvider modelProvider;

    @Test
    void mockModelIsRegistered() {
        var names = modelProvider.listModelNames(LlmService.class);
        assertTrue(names.contains("mock"),
                "the mock profile must register a model named 'mock'; got " + names);
    }

    @Test
    void producesAScheduleWithNoKeyAndNoNetwork() {
        var input = new UserInput(
                "I'm a senior platform engineer into Kubernetes, resilience and DevEx");

        var schedule = AgentInvocation
                .create(agentPlatform, PersonalSchedule.class)
                .invoke(input);

        assertNotNull(schedule, "the mock flow must produce a schedule");
        assertFalse(schedule.items().isEmpty(), "schedule should place the drafted sessions");
        assertFalse(schedule.rationale().isBlank(), "schedule must explain itself");
        assertTrue(schedule.getContent().contains("Kubernetes Without the Tears"),
                "rendered schedule should name a chosen session");
    }
}
