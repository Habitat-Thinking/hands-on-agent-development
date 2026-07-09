package com.russmiles.confplanner.eval;

import com.embabel.agent.api.annotation.AchievesGoal;
import com.embabel.agent.api.annotation.Action;
import com.embabel.agent.api.annotation.Agent;
import com.embabel.agent.api.common.Ai;

/**
 * A tiny second agent that grades a schedule — the LLM-as-judge behind the eval lane.
 *
 * <p>It lives in <strong>test</strong> sources on purpose: the shipped app keeps its clean
 * two-goal story (schedule + networking), and the judge only exists when the eval test imports it
 * ({@code @Import(ScheduleJudge.class)}). One goal, one action: consume a {@link ScheduleUnderReview}
 * and produce a {@link ScheduleVerdict}. It uses the ordinary {@code Ai} idiom the other actions use
 * ({@code withLlmByRole("best")}) — grading judgement is exactly the kind of open-ended reasoning the
 * strong model is for (Lab 6).
 *
 * <p>Deliberately blunt in the prompt: judge honestly, score low when the picks are generic or
 * off-topic. A judge that rubber-stamps everything is worse than no eval.
 */
@Agent(description = "Grade a produced conference schedule against the attendee's request")
public class ScheduleJudge {

    @AchievesGoal(description = "Score a schedule's quality for the attendee it was built for")
    @Action
    ScheduleVerdict judge(ScheduleUnderReview review, Ai ai) {
        return ai
                .withLlmByRole("best")
                .creating(ScheduleVerdict.class)
                .fromPrompt("""
                        You are grading a personal conference schedule that an AI agent produced for
                        one attendee. Judge the quality of judgement only — not spelling or Markdown.
                        Score honestly and be willing to score low: a generic or off-topic schedule
                        should not pass.

                        Return:
                        - relevance (1-5): do the chosen sessions match the attendee's stated
                          interests and goals?
                        - balance (1-5): do they cover the attendee's themes with sensible variety,
                          rather than repeating one note?
                        - onProfile (true/false): is this clearly built for THIS attendee, not a
                          generic pick anyone would get?
                        - rationale: one or two sentences justifying the scores.

                        # The attendee's request
                        %s

                        # The schedule under review
                        %s
                        """.formatted(review.request(), review.renderedSchedule()));
    }
}
