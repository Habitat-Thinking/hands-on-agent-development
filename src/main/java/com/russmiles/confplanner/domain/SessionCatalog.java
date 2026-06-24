package com.russmiles.confplanner.domain;

import java.util.List;

/**
 * The whole conference: every session and every speaker.
 *
 * <p>Loaded once from bundled JSON by {@code CatalogService}. This is the "plain code" half of
 * the hybrid agent &mdash; there is nothing to reason about here, so no LLM is involved in
 * producing it. The action that yields a {@code SessionCatalog} is deterministic.
 */
public record SessionCatalog(
        List<Session> sessions,
        List<Speaker> speakers
) {
}
