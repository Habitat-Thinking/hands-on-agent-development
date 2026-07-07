package com.russmiles.confplanner.domain;

import java.util.List;

/**
 * A shortlist: the sessions that plausibly match the attendee's interests.
 *
 * <p>This is a distinct type from {@code SessionCatalog} on purpose. The catalog is everything;
 * candidates are the few worth scheduling. Giving the shortlist its own type is what lets the
 * planner (GOAP) know that "shortlist" must happen before "assemble" &mdash; the ordering falls out
 * of the types, it is never hard-wired.
 */
public record CandidateSessions(
        List<Session> sessions
) {
}
