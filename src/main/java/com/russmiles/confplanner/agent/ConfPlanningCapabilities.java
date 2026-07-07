package com.russmiles.confplanner.agent;

import com.embabel.agent.api.common.Ai;
import com.embabel.agent.domain.io.UserInput;
import com.russmiles.confplanner.domain.AttendeeProfile;
import com.russmiles.confplanner.domain.CandidateSessions;
import com.russmiles.confplanner.domain.ResearchedSessions;
import com.russmiles.confplanner.domain.Session;
import com.russmiles.confplanner.domain.SessionCatalog;
import com.russmiles.confplanner.domain.SessionInsight;
import com.russmiles.confplanner.service.CatalogService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * The shared planning pipeline, as plain helper methods (Lab 5).
 *
 * <p>In Embabel an {@code @Agent} plans only with the actions defined on its own class. So to let a
 * second goal (a {@code NetworkingPlan}) reuse the same understand → load → shortlist → research
 * steps as the schedule goal, the <em>logic</em> lives here once, and each agent declares thin
 * {@code @Action} wrappers that delegate to it. New capability, shared pipeline, no copy-pasted
 * logic &mdash; and the schedule goal's behaviour is unchanged (its integration test stays green).
 *
 * <p>This is a plain {@code @Service}: it contributes no actions itself, it just holds the prompts
 * and the id-resolution both agents need.
 */
@Service
public class ConfPlanningCapabilities {

    private final CatalogService catalogService;

    public ConfPlanningCapabilities(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    /** What the model returns when shortlisting: ids it picked, plus why. */
    public record Shortlisting(List<String> sessionIds, String reasoning) {
    }

    /** One researched insight as the model emits it (id-only; code resolves the Session). */
    public record Insight(String sessionId, String whyRelevant, double matchScore) {
    }

    /** What the model returns when researching: one insight per shortlisted id. */
    public record ResearchOutput(List<Insight> insights) {
    }

    /** Understand the attendee from free text (LLM). Cheap work — pulling fields from a sentence. */
    public AttendeeProfile extractProfile(UserInput userInput, Ai ai) {
        return ai
                .withLlmByRole("cheapest")
                .creating(AttendeeProfile.class)
                .fromPrompt("""
                        Read the attendee's request and extract a structured profile.

                        - interests: concrete topics/technologies they mention (lower-case tags)
                        - role: their job role if stated, else a reasonable guess
                        - experienceLevel: one of Beginner, Intermediate, Advanced
                        - goals: what they want out of the conference, as short phrases
                        - avoidTopics: topics/tags the attendee explicitly does NOT want
                          (e.g. "no vendor keynotes" -> ["vendor", "keynote"]); empty if none

                        # Attendee request
                        %s
                        """.formatted(userInput.getContent()));
    }

    /** The whole (synthetic) catalog. Deterministic — no LLM. */
    public SessionCatalog catalog() {
        return catalogService.catalog();
    }

    /** Shortlist sessions matching the attendee (LLM), honouring avoidTopics. */
    public CandidateSessions shortlist(AttendeeProfile profile, SessionCatalog catalog, Ai ai) {
        // Belt: drop avoided sessions in plain code so the rule holds even if the model slips.
        var menu = catalog.sessions().stream()
                .filter(s -> !profile.shouldAvoid(s))
                .map(ConfPlanningCapabilities::menuLine)
                .collect(Collectors.joining("\n"));

        // Braces: attach the profile as a PromptContributor so its avoid-list rides along in
        // the prompt automatically — the rule lives on the domain object (DICE), not here.
        // Cheap work: matching tags to a menu. Route to the cheapest model (Lab 6).
        var shortlisting = ai
                .withLlmByRole("cheapest")
                .withPromptContributor(profile)
                .creating(Shortlisting.class)
                .fromPrompt("""
                        Pick the 8-14 sessions from the catalog that best match this attendee.
                        Favour their interests, role and goals. Return only the session ids.

                        # Attendee
                        interests: %s
                        role: %s
                        experience: %s
                        goals: %s

                        # Catalog (id: title [tags] (track, level))
                        %s
                        """.formatted(
                        profile.interests(), profile.role(),
                        profile.experienceLevel(), profile.goals(), menu));

        return new CandidateSessions(resolve(catalog.sessions(), shortlisting.sessionIds()));
    }

    /** Research each shortlisted session (LLM): why relevant + a match score. */
    public ResearchedSessions research(CandidateSessions candidates, Ai ai) {
        var menu = candidates.sessions().stream()
                .map(ConfPlanningCapabilities::menuLine)
                .collect(Collectors.joining("\n"));

        // Reasoning about relevance and scoring — route to the strong model (Lab 6).
        var output = ai
                .withLlmByRole("best")
                .creating(ResearchOutput.class)
                .fromPrompt("""
                        For each shortlisted session, say in one sentence why it is relevant to an
                        attendee and give a matchScore between 0.0 and 1.0. Return one entry per id.

                        # Shortlist (id: title [tags] (track, level))
                        %s
                        """.formatted(menu));

        var byId = candidates.sessions().stream()
                .collect(Collectors.toMap(Session::id, Function.identity(), (a, b) -> a));
        var insights = (output.insights() == null ? List.<Insight>of() : output.insights()).stream()
                .filter(i -> byId.containsKey(i.sessionId()))
                .map(i -> new SessionInsight(byId.get(i.sessionId()), i.whyRelevant(), i.matchScore()))
                .toList();
        return new ResearchedSessions(insights);
    }

    static String menuLine(Session s) {
        return "%s: %s [%s] (%s, %s)".formatted(
                s.id(), s.title(), String.join(",", s.tags()), s.track(), s.level());
    }

    /** Resolve model-chosen ids back to catalog sessions, ignoring anything it invented. */
    static List<Session> resolve(List<Session> sessions, List<String> ids) {
        if (ids == null) {
            return List.of();
        }
        Map<String, Session> byId = sessions.stream()
                .collect(Collectors.toMap(Session::id, Function.identity(), (a, b) -> a));
        return ids.stream().map(byId::get).filter(java.util.Objects::nonNull).toList();
    }
}
