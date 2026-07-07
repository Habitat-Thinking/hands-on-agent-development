package com.russmiles.confplanner.domain;

import java.util.List;

/**
 * What we know about the attendee we are planning for.
 *
 * <p>This is the first thing the agent produces from free text, and the lens through
 * which every later step is judged: which sessions to shortlist, how to resolve clashes,
 * what to say in the rationale. Modelling the attendee explicitly &mdash; rather than passing
 * a bag of strings around &mdash; is the "Model the domain first" habit (DICE) in practice.
 *
 * <p>Lab 1 evolves this record: it gains {@code avoidTopics} and starts contributing to the
 * prompt itself (see {@code PromptContributor}). In the baseline it is a plain data carrier.
 */
public record AttendeeProfile(
        List<String> interests,
        String role,
        String experienceLevel,
        List<String> goals
        // TODO (Lab 1): add `List<String> avoidTopics` here, then make this record a
        //   PromptContributor so the avoid-list travels with the domain object into the prompt.
        //   See labs/lab1-dice.md. The after-state lives on branch lab1-after.
) {
}
