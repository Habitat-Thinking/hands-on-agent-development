package com.russmiles.confplanner.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.domain.io.UserInput;
import com.russmiles.confplanner.domain.AttendeeProfile;
import com.russmiles.confplanner.domain.CandidateSessions;
import com.russmiles.confplanner.domain.NetworkingPlan;
import com.russmiles.confplanner.domain.ResearchedSessions;
import com.russmiles.confplanner.domain.SessionCatalog;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Suggests who an attendee should try to meet at the conference (Lab 5).
 *
 * <p>A <em>new agent</em>, added without editing {@link ConfPlannerAgent}'s goal. Its single goal is
 * a {@link NetworkingPlan}. Because an Embabel agent plans only with its own actions, this agent
 * declares the same understand → load → shortlist → research wrappers — but they delegate to the
 * shared {@link ConfPlanningCapabilities}, so there is no duplicated logic. The planner derives
 * {@code extract → loadCatalog → shortlist → research → planNetworking}: the existing pipeline,
 * reused to reach a brand-new goal. Extend by adding, not rewiring.
 */
@Agent(description = "Suggest who an attendee should meet at the conference")
public class ConfNetworkingAgent {

    private final ConfPlanningCapabilities steps;

    public ConfNetworkingAgent(ConfPlanningCapabilities steps) {
        this.steps = steps;
    }

    /** What the model returns: names to meet plus the reasoning. */
    record NetworkingDraft(List<String> peopleToMeet, String rationale) {
    }

    // --- Shared pipeline (delegates to ConfPlanningCapabilities) --------------------------

    @Action
    AttendeeProfile extractAttendeeProfile(UserInput userInput, Ai ai) {
        return steps.extractProfile(userInput, ai);
    }

    @Action
    SessionCatalog loadCatalog(AttendeeProfile profile) {
        return steps.catalog();
    }

    @Action
    CandidateSessions shortlistSessions(AttendeeProfile profile, SessionCatalog catalog, Ai ai) {
        return steps.shortlist(profile, catalog, ai);
    }

    @Action
    ResearchedSessions researchSessions(CandidateSessions candidates, Ai ai) {
        return steps.research(candidates, ai);
    }

    // --- The new goal --------------------------------------------------------------------

    @AchievesGoal(description = "Produce a networking plan of people to meet")
    @Action
    NetworkingPlan planNetworking(AttendeeProfile profile, ResearchedSessions researched, Ai ai) {
        var speakers = researched.insights().stream()
                .flatMap(i -> i.session().speakers().stream())
                .distinct()
                .collect(Collectors.joining(", "));

        var draft = ai
                .withDefaultLlm()
                .withPromptContributor(profile)
                .creating(NetworkingDraft.class)
                .fromPrompt("""
                        Suggest up to five people this attendee should try to meet, drawn from the
                        speakers of their shortlisted sessions. Favour their interests and goals.
                        Return the names and a short rationale.

                        # Attendee
                        interests: %s
                        goals: %s

                        # Speakers across the researched sessions
                        %s
                        """.formatted(profile.interests(), profile.goals(), speakers));

        return new NetworkingPlan(draft.peopleToMeet(), draft.rationale());
    }
}
