package com.russmiles.confplanner.domain;

import java.util.List;

/**
 * A proposed schedule that has not yet passed the guardrails (Lab 3).
 *
 * <p>The {@code assembleSchedule} action produces a {@code DraftSchedule}; the goal action
 * {@code confirmSchedule} consumes it but only runs when the {@code noDoubleBooking} invariant
 * holds. Separating "draft" from "confirmed" is what lets the invariant actually <em>bite</em> at
 * runtime: a clashing draft never satisfies the goal's precondition, so the planner re-runs
 * assembly rather than handing back a broken schedule.
 */
public record DraftSchedule(
        List<ScheduleItem> items,
        String rationale
) {
}
