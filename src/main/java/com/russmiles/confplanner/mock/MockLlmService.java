package com.russmiles.confplanner.mock;

import com.embabel.agent.core.Usage;
import com.embabel.agent.spi.LlmService;
import com.embabel.agent.spi.loop.LlmMessageResponse;
import com.embabel.agent.spi.loop.LlmMessageSender;
import com.embabel.agent.spi.loop.streaming.LlmMessageStreamer;
import com.embabel.chat.AssistantMessageWithToolCalls;
import com.embabel.chat.Message;
import com.embabel.common.ai.model.LlmOptions;
import com.embabel.common.ai.model.PricingModel;
import com.embabel.common.ai.prompt.PromptContributor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.russmiles.confplanner.agent.ConfPlanningCapabilities;
import com.russmiles.confplanner.domain.AttendeeProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

/**
 * A deterministic, in-JVM stand-in for a real LLM, active only under the {@code mock} Spring
 * profile. It exists so a learner can run the whole ConfPlanner flow with <em>no API key and no
 * network call</em>: {@code SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run}, then {@code x "..."}
 * produces a real {@code PersonalSchedule}.
 *
 * <p>Why a bean rather than a platform flag? The 0.5.0 property
 * {@code embabel.agent.platform.test.mock-mode} is inert &mdash; no runtime class consults it. The
 * live seam is {@link com.embabel.common.ai.model.ConfigurableModelProvider}, which collects every
 * {@link LlmService} bean from the Spring context. So we register one stub {@code LlmService} named
 * {@code "mock"} and point all model roles at it in {@code application.yml} (under the mock profile).
 *
 * <p>The meat is {@link #createMessageSender}: it returns a sender that concatenates the prompt
 * messages, matches known ConfPlanner prompt fragments, and returns canned JSON text for the type
 * that call expects. Embabel parses that text into the target record &mdash; the sender only needs
 * to match the prompt and return the right JSON. The fragment&rarr;shape mapping mirrors, exactly,
 * the choreography in {@code ConfPlannerAgentIntegrationTest} / {@code ConfNetworkingAgentIntegrationTest}.
 *
 * <p>This bean is {@code @Profile("mock")} only, so the keyless {@code ./mvnw verify} (which does
 * not activate {@code mock}) never sees it and is entirely unaffected.
 */
@Component
@Profile("mock")
public class MockLlmService implements LlmService<MockLlmService> {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public LlmMessageSender createMessageSender(LlmOptions options) {
        return (messages, tools) -> {
            var prompt = messages.stream()
                    .map(Message::getContent)
                    .reduce("", (a, b) -> a + "\n" + b);
            var json = cannedJsonFor(prompt);
            // The tool loop casts the response message to AssistantMessageWithToolCalls; we never
            // call a tool, so it carries the canned text with an empty tool-call list.
            var message = new AssistantMessageWithToolCalls(json, List.of());
            return new LlmMessageResponse(message, json, new Usage(0, 0, null));
        };
    }

    /**
     * Match a known ConfPlanner prompt fragment and return valid JSON for the record that call
     * creates. Same fragments as the Mockito integration tests, but JSON instead of objects. An
     * unmatched prompt yields {@code {}} rather than an error, so a stray call degrades gracefully.
     */
    private String cannedJsonFor(String prompt) {
        if (prompt.contains("extract a structured profile")) {
            return json(new AttendeeProfile(
                    List.of("kubernetes", "resilience", "developer-experience"),
                    "Senior Platform Engineer", "Advanced",
                    List.of("level up platform work"), List.of()));
        }
        if (prompt.contains("Pick the 8-14 sessions")) {
            return json(new ConfPlanningCapabilities.Shortlisting(
                    List.of("PC-01", "PC-02", "PC-03", "SR-09"), "match interests"));
        }
        if (prompt.contains("why it is relevant")) {
            return json(new ConfPlanningCapabilities.ResearchOutput(List.of(
                    new ConfPlanningCapabilities.Insight("PC-01", "core platform topic", 0.9),
                    new ConfPlanningCapabilities.Insight("PC-02", "golden paths", 0.8),
                    new ConfPlanningCapabilities.Insight("PC-03", "cost awareness", 0.7),
                    new ConfPlanningCapabilities.Insight("SR-09", "resilience patterns", 0.85))));
        }
        if (prompt.contains("Build this attendee a personal schedule")) {
            // ConfPlannerAgent.ScheduleDraft(List<String> sessionIds, String rationale) is
            // package-private; mirror its shape by field name so the JSON deserialises the same.
            return json(new ScheduleDraftShape(
                    List.of("PC-01", "PC-02", "PC-03"),
                    "Three platform-leaning sessions across three days, no clashes."));
        }
        if (prompt.contains("Suggest up to five people")) {
            // ConfNetworkingAgent.NetworkingDraft(List<String> peopleToMeet, String rationale).
            return json(new NetworkingDraftShape(
                    List.of("Dr. Priya Venkatasubramanian", "Marcus Oduya"),
                    "Both speak to your platform and resilience goals."));
        }
        if (prompt.contains("premium briefing")) {
            // PremiumBriefing(String summary).
            return json(new PremiumBriefingShape(
                    "A concise cross-session briefing covering platform, cost and resilience themes."));
        }
        return "{}";
    }

    /** Mirrors {@code ConfPlannerAgent.ScheduleDraft} (package-private) by field name. */
    private record ScheduleDraftShape(List<String> sessionIds, String rationale) {
    }

    /** Mirrors {@code ConfNetworkingAgent.NetworkingDraft} (package-private) by field name. */
    private record NetworkingDraftShape(List<String> peopleToMeet, String rationale) {
    }

    /** Mirrors {@code PremiumBriefing} by field name. */
    private record PremiumBriefingShape(String summary) {
    }

    private static String json(Object value) {
        try {
            return MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            // Never propagate: a serialisation slip should degrade to empty JSON, not crash a demo.
            return "{}";
        }
    }

    @Override
    public LlmMessageStreamer createMessageStreamer(LlmOptions options) {
        throw new UnsupportedOperationException("mock model does not stream");
    }

    @Override
    public boolean supportsStreaming() {
        return false;
    }

    @Override
    public MockLlmService withKnowledgeCutoffDate(LocalDate date) {
        return this;
    }

    @Override
    public MockLlmService withPromptContributor(PromptContributor promptContributor) {
        return this;
    }

    @Override
    public List<PromptContributor> getPromptContributors() {
        return List.of();
    }

    @Override
    public String getName() {
        return "mock";
    }

    @Override
    public String getProvider() {
        return "mock";
    }

    @Override
    public LocalDate getKnowledgeCutoffDate() {
        return LocalDate.of(2026, 1, 1);
    }

    @Override
    public PricingModel getPricingModel() {
        return PricingModel.usdPerToken(0.0, 0.0);
    }
}
