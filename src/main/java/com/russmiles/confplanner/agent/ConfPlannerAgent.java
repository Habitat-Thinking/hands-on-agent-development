package com.russmiles.confplanner.agent;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.annotation.Condition;
import com.embabel.agent.api.common.Ai;
import com.embabel.agent.domain.io.UserInput;
import com.embabel.agent.mcpserver.security.SecureAgentTool;
import com.russmiles.confplanner.domain.AttendeeProfile;
import com.russmiles.confplanner.domain.CandidateSessions;
import com.russmiles.confplanner.domain.DraftSchedule;
import com.russmiles.confplanner.domain.PersonalSchedule;
import com.russmiles.confplanner.domain.PremiumBriefing;
import com.russmiles.confplanner.domain.ResearchedSessions;
import com.russmiles.confplanner.domain.ScheduleItem;
import com.russmiles.confplanner.domain.SessionCatalog;
import com.russmiles.confplanner.domain.SessionInsight;

import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Turns a free-text attendee request into a conflict-free personal UberConf schedule.
 *
 * <p>The agent is defined as a set of typed {@code @Action}s; the planner (GOAP) derives the order
 * from the types — nothing is hard-wired. The understand → load → shortlist → research steps are
 * thin wrappers that delegate to the shared {@link ConfPlanningCapabilities} service, so the very
 * same pipeline also feeds the networking goal ({@link ConfNetworkingAgent}) without copy-pasting
 * any logic. The inferred plan:
 *
 * <pre>
 *   UserInput -&gt; extractAttendeeProfile -&gt; loadCatalog -&gt; shortlistSessions -&gt; researchSessions
 *             -&gt; assembleSchedule (DraftSchedule) -&gt; confirmSchedule (PersonalSchedule)
 * </pre>
 *
 * <p>Run it from the shell with {@code x "I'm a senior platform engineer into Kubernetes,
 * resilience and DevEx; build me a schedule"}.
 */
@Agent(description = "Produce a conflict-free personal UberConf schedule from a free-text request")
public class ConfPlannerAgent {

    private final ConfPlanningCapabilities steps;

    public ConfPlannerAgent(ConfPlanningCapabilities steps) {
        this.steps = steps;
    }

    /** What the model returns when assembling: an ordered, clash-free set of ids, plus the rationale. */
    record ScheduleDraft(List<String> sessionIds, String rationale) {
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

    @Action(post = {"hasCandidates"})
    CandidateSessions shortlistSessions(AttendeeProfile profile, SessionCatalog catalog, Ai ai) {
        return steps.shortlist(profile, catalog, ai);
    }

    @Action
    ResearchedSessions researchSessions(CandidateSessions candidates, Ai ai) {
        return steps.research(candidates, ai);
    }

    // --- Guardrails (Lab 3) ---------------------------------------------------------------
    //   The invariant only bites if the GOAL depends on it. assembleSchedule produces a
    //   DraftSchedule (post = noDoubleBooking, canRerun = true) and the goal action
    //   confirmSchedule requires noDoubleBooking. A clashing draft never satisfies the goal, so the
    //   planner re-runs assembly; with no clash-free option it stops at the budget.

    @Condition(name = "hasCandidates")
    boolean hasCandidates(CandidateSessions candidates) {
        return candidates != null && !candidates.sessions().isEmpty();
    }

    @Condition(name = "noDoubleBooking")
    boolean noDoubleBooking(DraftSchedule draft) {
        var slots = draft.items().stream().map(ScheduleItem::slot).toList();
        return slots.size() == new HashSet<>(slots).size();
    }

    // --- Assemble a draft schedule (LLM) -------------------------------------------------

    @Action(pre = {"hasCandidates"}, post = {"noDoubleBooking"}, canRerun = true)
    DraftSchedule assembleSchedule(AttendeeProfile profile, ResearchedSessions researched, Ai ai) {
        var sessions = researched.insights().stream().map(SessionInsight::session).toList();
        var menu = researched.insights().stream()
                .map(i -> ConfPlanningCapabilities.menuLine(i.session()) + " @ " + i.session().slot()
                        + " — score " + i.matchScore() + " — " + i.whyRelevant())
                .collect(Collectors.joining("\n"));

        var draft = ai
                .withDefaultLlm()
                .creating(ScheduleDraft.class)
                .fromPrompt("""
                        Build this attendee a personal schedule from the shortlisted sessions.

                        Hard rule: never pick two sessions in the same slot (the "@ <day time>"
                        suffix is the slot). Prefer their stated goals when a slot has a clash.
                        Return the chosen session ids and a short rationale explaining the picks.

                        # Attendee goals
                        %s

                        # Shortlist (id: title [tags] (track, level) @ slot)
                        %s
                        """.formatted(profile.goals(), menu));

        // Each item goes in its session's real slot, so distinct sessions occupy distinct slots
        // and noDoubleBooking can hold. (Lab 4 fixed this — see lab4-broken for the fault.)
        var items = ConfPlanningCapabilities.resolve(sessions, draft.sessionIds()).stream()
                .map(s -> new ScheduleItem(s, s.slot()))
                .toList();
        return new DraftSchedule(items, draft.rationale());
    }

    // --- Confirm the schedule — the goal, gated by the invariant -------------------------

    @AchievesGoal(description = "Produce a conflict-free personal schedule")
    @Action(pre = {"noDoubleBooking"})
    PersonalSchedule confirmSchedule(DraftSchedule draft) {
        // Only reachable when noDoubleBooking holds, so the goal is conflict-free by construction.
        return new PersonalSchedule(draft.items(), draft.rationale());
    }

    // --- Secured premium tool (Lab 3) -----------------------------------------------------
    //   @SecureAgentTool gates this action with a Spring-Security authority expression. Enforced
    //   when exposed over MCP with an authenticated caller; without 'conf:premium' the call is
    //   denied BEFORE any LLM spend. PremiumBriefing is off the schedule goal path.

    @SecureAgentTool("hasAuthority('conf:premium')")
    @Action
    PremiumBriefing premiumBriefing(ResearchedSessions researched, Ai ai) {
        var titles = researched.insights().stream()
                .map(i -> i.session().title())
                .collect(Collectors.joining("; "));
        return ai
                .withDefaultLlm()
                .creating(PremiumBriefing.class)
                .fromPrompt("Write a one-paragraph premium briefing across these sessions: " + titles);
    }
}
