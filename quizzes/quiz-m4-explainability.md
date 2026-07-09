# Quiz — M4: Explainability (Lab 4)

> Section: **Explainability — debug by reading state**. Habit: **Read the plan, not the vibes.**
> Four short questions. Attempt them before opening the answers.

## Questions

1. "The agent is STUCK." Mechanically, what is it *doing* — a hang, an infinite loop, or something
   else? What line names it?
2. On `lab4-broken`, exactly one condition never flips to TRUE. Which one — and what does its name
   tell you the root cause is?
3. `lab4-broken` is the one branch where `./mvnw verify` is *supposed* to fail. Can you diagnose the
   stall with **no API key**? How?
4. Transfer: when an agent you work on fails, is the failure *legible* — can you name the condition
   that stayed false, or are you reduced to re-running and guessing?

<details>
<summary>Answers &amp; discussion</summary>

1. **A bounded spin, not a hang.** The planner re-runs `assembleSchedule` over and over until
   `early termination by MaxActionsEarlyTerminationPolicy(maxActions=50) … error=true` stops it. The
   literal word `STUCK` appears only in the `[flight-recorder]` summary line.
2. **`noDoubleBooking` stays FALSE.** The fault pins every item to the placeholder slot `"TBD"`, so
   all items share one slot; the invariant is never satisfied, so `confirmSchedule`'s precondition is
   never met and the goal can never complete. The name told you it's about two things sharing a slot.
3. **Yes.** Run `./mvnw test` (or `-Dtest=ConfPlannerAgentIntegrationTest`) on `lab4-broken` — the
   mocked integration test's output *is* the planning log, with the budget-stop and world-state lines.
   The stall is deterministic Java, so no model is needed. (With Docker, `mock,observability` +
   `plan` shows the same loop in Zipkin.)
4. *(Your answer.)* The bar: a stuck agent should be a *legible* failure — the world-state names the
   condition that stayed false. If yours fails opaquely, that's the seam to instrument.

</details>
