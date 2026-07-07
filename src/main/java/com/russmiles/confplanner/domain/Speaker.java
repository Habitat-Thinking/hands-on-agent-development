package com.russmiles.confplanner.domain;

import java.util.List;

/**
 * A conference speaker. Entirely fictional &mdash; see the synthetic catalog.
 *
 * <p>Speakers carry the topics they are known for, which Lab 5's networking plan uses to
 * suggest who an attendee might want to meet.
 */
public record Speaker(
        String name,
        String bio,
        List<String> topics
) {
}
