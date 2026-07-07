package com.russmiles.confplanner.domain;

import java.util.List;

/**
 * The shortlist, now annotated with research: each candidate paired with why it is relevant and
 * how well it scores.
 *
 * <p>Introduced in Lab 2. Because {@code assembleSchedule} is changed to consume this type instead
 * of {@code CandidateSessions}, the planner derives that {@code researchSessions} (which produces
 * it) must run first &mdash; no flow order is written by hand.
 */
public record ResearchedSessions(
        List<SessionInsight> insights
) {
}
