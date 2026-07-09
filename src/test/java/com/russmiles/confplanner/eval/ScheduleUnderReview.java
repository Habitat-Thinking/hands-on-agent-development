package com.russmiles.confplanner.eval;

/**
 * The seed the judge agent plans from: the original request plus the rendered schedule to grade.
 *
 * <p>Everything the judge needs is text — the attendee's own words and the schedule's Markdown
 * (titles, tracks, slots, and the agent's rationale, via {@code PersonalSchedule.getContent()}). The
 * judge is invoked exactly like the scheduler — {@code AgentInvocation.create(platform,
 * ScheduleVerdict.class).invoke(review)} — so the eval lane introduces no new framework surface,
 * just a second goal seeded with this record.
 *
 * @param request          the free-text attendee request the schedule was built for.
 * @param renderedSchedule the produced schedule as Markdown (from {@code getContent()}).
 */
public record ScheduleUnderReview(
        String request,
        String renderedSchedule
) {
}
