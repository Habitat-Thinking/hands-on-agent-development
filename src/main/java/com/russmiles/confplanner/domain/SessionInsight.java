package com.russmiles.confplanner.domain;

/**
 * A researched verdict on one shortlisted session: why it matters to this attendee, and how
 * strong the match is.
 *
 * <p>Introduced in Lab 2. It is the unit of value the {@code researchSessions} action produces and
 * the {@code assembleSchedule} action consumes &mdash; the new produces/consumes link that makes the
 * planner route through research.
 *
 * @param matchScore 0.0&ndash;1.0; higher means a better fit for the attendee's goals.
 */
public record SessionInsight(
        Session session,
        String whyRelevant,
        double matchScore
) {
}
