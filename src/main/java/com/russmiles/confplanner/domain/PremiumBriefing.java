package com.russmiles.confplanner.domain;

/**
 * A premium, paid-tier briefing about the conference (Lab 3).
 *
 * <p>This type exists to carry the output of a <em>secured</em> tool. It is deliberately not
 * consumed by the schedule goal, so the planner never schedules the premium action when planning a
 * {@code PersonalSchedule} &mdash; the access-control demo cannot perturb the free flow.
 */
public record PremiumBriefing(
        String summary
) {
}
