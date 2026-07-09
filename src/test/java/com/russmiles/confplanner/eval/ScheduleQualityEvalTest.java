package com.russmiles.confplanner.eval;

import com.embabel.agent.api.invocation.AgentInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.core.Budget;
import com.embabel.agent.core.ProcessOptions;
import com.embabel.agent.domain.io.UserInput;
import com.russmiles.confplanner.domain.PersonalSchedule;
import com.russmiles.confplanner.domain.ScheduleItem;
import com.russmiles.confplanner.domain.Session;
import com.russmiles.confplanner.service.CatalogService;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * The eval lane: the sampled, judgement-quality half of the workshop's test strategy.
 *
 * <p>Everything else in the suite mocks the model and asserts on the deterministic <em>scaffolding</em>
 * — plan derivation, guardrail gates, world-state. Those prove the schedule is <em>conflict-free</em>,
 * never that it is <em>good</em>. This lane closes that gap for a small golden set: it runs the real
 * agent against a real model, applies deterministic gates as a belt, and then asks a strong model
 * (the {@link ScheduleJudge}) whether each schedule is actually relevant, balanced, and on-profile.
 *
 * <p><strong>Why it is tagged {@code @Tag("eval")} and excluded from {@code verify}:</strong> it makes
 * real, non-deterministic, paid model calls and needs a provider key. The default keyless build must
 * stay green and deterministic, so this never runs there. Run it deliberately:
 *
 * <pre>
 *   OPENAI_API_KEY=sk-... ./mvnw -Peval test
 * </pre>
 *
 * <p>With no key the live assertions are skipped via a JUnit assumption (reported as skipped, not
 * failed) — the context still boots, so the wiring stays honest even key-free. See
 * {@code docs/how-to/run-the-eval-lane.md}.
 */
@Tag("eval")
@SpringBootTest(properties = {
        "spring.shell.interactive.enabled=false",
        "embabel.agent.shell.interactive.enabled=false"
})
@Import(ScheduleJudge.class)
class ScheduleQualityEvalTest {

    @Autowired
    private AgentPlatform agentPlatform;

    @Autowired
    private CatalogService catalogService;

    @Autowired
    private Environment environment;

    /** One golden case: a request, the tags it must never contain, and the sane item-count band. */
    record GoldenCase(String name, String request, List<String> mustAvoidTags, int minItems, int maxItems) {
        @Override
        public String toString() {
            return name;
        }
    }

    static List<GoldenCase> goldenCases() {
        return List.of(
                new GoldenCase(
                        "platform-engineer",
                        "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule",
                        List.of(), 2, 8),
                new GoldenCase(
                        "security-no-vendor-keynotes",
                        "I'm an application security engineer focused on threat modelling and supply-chain "
                                + "security — but absolutely no vendor keynotes; build me a schedule",
                        List.of("vendor"), 2, 8),
                new GoldenCase(
                        "data-ml-observability",
                        "I'm a data/ML engineer interested in LLM evaluation, data pipelines and "
                                + "observability; build me a schedule",
                        List.of(), 2, 8));
    }

    @ParameterizedTest(name = "{0}")
    @MethodSource("goldenCases")
    void scheduleMeetsQualityBar(GoldenCase gc) {
        // Resolve the key from the Spring Environment, which covers both a real environment
        // variable and a key placed in .env (spring-dotenv loads .env into the Environment).
        var key = environment.getProperty("OPENAI_API_KEY");
        assumeTrue(key != null && !key.isBlank() && !key.startsWith("noop-"),
                "eval lane needs a provider key — set OPENAI_API_KEY (env or .env) and run ./mvnw -Peval test");

        // Run the REAL agent against a REAL model (Lab 3 budget: it cannot exceed the cap).
        var options = ProcessOptions.DEFAULT.withBudget(new Budget(0.50, 20, 200_000));
        PersonalSchedule schedule = AgentInvocation
                .builder(agentPlatform)
                .options(options)
                .build(PersonalSchedule.class)
                .invoke(new UserInput(gc.request()));

        // --- Deterministic gates (a belt over the model's judgement) ------------------------
        assertNotNull(schedule, "the agent must produce a schedule");
        int n = schedule.items().size();
        assertTrue(n >= gc.minItems() && n <= gc.maxItems(),
                "schedule has " + n + " sessions, expected " + gc.minItems() + ".." + gc.maxItems());

        var slots = schedule.items().stream().map(ScheduleItem::slot).toList();
        assertEquals(slots.size(), new HashSet<>(slots).size(), "no two sessions may share a slot");

        Set<String> catalogIds = catalogService.catalog().sessions().stream()
                .map(Session::id).collect(Collectors.toSet());
        schedule.items().forEach(item -> assertTrue(catalogIds.contains(item.session().id()),
                "scheduled session must exist in the catalog: " + item.session().id()));

        for (var item : schedule.items()) {
            for (var avoid : gc.mustAvoidTags()) {
                assertFalse(
                        item.session().tags().stream().anyMatch(t -> t.equalsIgnoreCase(avoid)),
                        "avoided topic '" + avoid + "' must not appear: " + item.session().title());
            }
        }

        // --- Sampled LLM-as-judge (the quality half the mocked tests can't cover) ------------
        var review = new ScheduleUnderReview(gc.request(), schedule.getContent());
        ScheduleVerdict verdict = AgentInvocation
                .create(agentPlatform, ScheduleVerdict.class)
                .invoke(review);

        assertTrue(verdict.relevance() >= 3,
                "relevance " + verdict.relevance() + "/5 below bar — " + verdict.rationale());
        assertTrue(verdict.balance() >= 3,
                "balance " + verdict.balance() + "/5 below bar — " + verdict.rationale());
        assertTrue(verdict.onProfile(),
                "judge says the schedule is not on-profile — " + verdict.rationale());
    }
}
