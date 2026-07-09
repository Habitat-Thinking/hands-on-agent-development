# Quiz — M2: GOAP (Lab 2)

> Section: **GOAP — name the goal, not the steps**. Habit: **Name the goal, not the steps.**
> Four short questions. Attempt them before opening the answers.

## Questions

1. You add a `researchSessions` action and **reorder nothing**, yet the plan runs it *between*
   shortlist and assemble. What forces it into that position?
2. With **no API key**, how can you actually *see* the plan re-derive — where does the derived
   sequence appear?
3. You temporarily `return null;` from `researchSessions` and re-run. Does the agent crash? What does
   it do instead — and is that the *model's* behaviour or the *planner's*?
4. Transfer: where in a system you work on is the step order hard-wired in a way that would fight a
   change like inserting one new step?

<details>
<summary>Answers &amp; discussion</summary>

1. **A data dependency.** You re-pointed `assembleSchedule` to consume `ResearchedSessions`, and only
   `researchSessions` produces that type — so the planner must run research first to satisfy assemble.
   You declared *what each step needs and produces*; GOAP derived the order. The order is a
   consequence, not a commitment.
2. In the **`formulated plan:` line** printed by the mocked integration test's output (run
   `./mvnw -q verify` or the single test and read the log), or in the mock shell:
   `SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run` then `plan "..."`. The plan re-derivation is
   fully keyless.
3. **It doesn't crash — it replans.** `ResearchedSessions` never becomes TRUE, so assemble's
   precondition is never met and the planner re-derives from the current world-state looking for
   another route. That's **deterministic planner** behaviour (the same mechanism as Lab 4's STUCK),
   not the model — so it's watchable offline.
4. *(Your answer.)* Look for an imperative sequence (a service method that calls step1→step2→step3)
   where adding a step means editing the call site rather than declaring a new need/product.

</details>
