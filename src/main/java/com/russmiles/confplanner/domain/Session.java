package com.russmiles.confplanner.domain;

import java.util.List;

/**
 * A single conference session in the catalog (fictional data).
 *
 * <p>The fields that matter for scheduling are {@code day} + {@code startTime}: together they
 * form the time slot. Two sessions that share a slot cannot both be attended &mdash; that is the
 * conflict the double-booking guardrail in Lab 3 exists to catch. The catalog deliberately
 * contains overlaps so the invariant has something to enforce.
 *
 * @param tags coarse subject tags (e.g. "kubernetes", "security") used for matching and
 *             for the avoid-topics filter introduced in Lab 1.
 */
public record Session(
        String id,
        String title,
        String abstractText,
        List<String> speakers,
        String track,
        String room,
        String day,
        String startTime,
        String endTime,
        String level,
        List<String> tags
) {
    /** The slot key used to detect clashes: a session occupies exactly one (day, startTime). */
    public String slot() {
        return day + " " + startTime;
    }
}
