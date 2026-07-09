# Quiz — M3: Guardrails (Lab 3)

> Section: **Guardrails — make the contract explicit**. Habit: **Make the contract explicit.**
> Four short questions. Attempt them before opening the answers.

## Questions

1. The trap slide: you put `@Action(post = {"noDoubleBooking"})` on the goal action. Does that stop a
   double-booked schedule being returned? Why or why not — and what *does* make the invariant bite?
2. Name the guardrail shapes Lab 3 introduces. (There are four that guard the *plan*, plus a fifth.)
3. `RequestContentGuardRail` screens the request for prompt-injection markers. Is it a **security
   boundary**? If a determined injection gets *past* it, what still stops the agent handing back a
   double-booked or corrupted-invariant schedule?
4. Transfer: name one invariant in a system you work on that is currently only a *polite request* in a
   prompt. What would it take to make it *bite*?

<details>
<summary>Answers &amp; discussion</summary>

1. **No.** A `post` on an `@AchievesGoal` action is a *planning promise*, not a runtime gate — the goal
   counts as achieved the moment its output type exists, whether or not the post still holds. To make
   it bite, some action the goal depends on must **require** the invariant as a `pre`: that's the
   assemble→confirm split (assemble produces a `DraftSchedule` with `post = noDoubleBooking` and
   `canRerun = true`; `confirmSchedule` is the goal with `pre = noDoubleBooking`). A clashing draft
   never satisfies the goal, so the planner re-runs assembly and stops at the budget if it can't.
2. **Invariant** (a postcondition the goal pre-requires), **precondition**, **budget**, **access
   control** (`@SecureAgentTool`) — plus the fifth shape, a **content guardrail**
   (`RequestContentGuardRail`).
3. **No — it's a cheap deterministic pre-filter (a small deny-list), trivially bypassed by
   rephrasing.** What still holds is the **structural invariant**: `noDoubleBooking` is deterministic
   code the model cannot author, so even a fully poisoned `assembleSchedule` can't produce a schedule
   that reaches the goal — the guarantee holds whether or not the filter caught anything.
4. *(Your answer.)* The move is: stop *asking* the model in a prompt, make the invariant a condition
   the goal (or a downstream gate) structurally requires, and add a test that proves a violating input
   is refused.

</details>
