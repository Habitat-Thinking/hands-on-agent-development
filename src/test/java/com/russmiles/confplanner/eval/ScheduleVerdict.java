package com.russmiles.confplanner.eval;

/**
 * The LLM-as-judge's structured verdict on a produced schedule.
 *
 * <p>This is the "sampled evaluation of judgement" half of the workshop's test strategy — the
 * complement to the deterministic gates that mock the model. The deterministic tests prove the
 * <em>scaffolding</em> holds (conflict-free, plan completes); this asks a strong model whether the
 * schedule is actually <em>good</em> for the attendee. Scores are 1–5; a run must clear the bar the
 * eval test sets. Because it is a judgement call, it needs a real model — which is exactly why the
 * eval lane lives outside the keyless {@code verify} gate.
 *
 * @param relevance 1–5: how well the chosen sessions match the attendee's stated interests and goals.
 * @param balance   1–5: sensible variety across the attendee's themes rather than one note repeated.
 * @param onProfile  true if the schedule is clearly built for <em>this</em> attendee, not generic.
 * @param rationale  one or two sentences justifying the scores (read this when a run fails the bar).
 */
public record ScheduleVerdict(
        int relevance,
        int balance,
        boolean onProfile,
        String rationale
) {
}
