package com.russmiles.confplanner.domain;

/**
 * One chosen session placed in one slot of the attendee's personal schedule.
 *
 * @param slot the human-readable slot label (day + start time) this session occupies.
 */
public record ScheduleItem(
        Session session,
        String slot
) {
}
