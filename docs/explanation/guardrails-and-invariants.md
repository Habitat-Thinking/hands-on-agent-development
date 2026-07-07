# About guardrails and invariants

This is the page where a subtle, easy-to-miss fact about Embabel 0.5.0 becomes the whole lesson. If
you take only one idea away from ConfPlanner, take this one: **a postcondition on the goal action
does not protect you.** Understanding *why* — and what to do instead — is the difference between an
agent that *hopes* it never double-books and one that *cannot*.

## The failure mode guardrails exist to prevent

ConfPlanner's job is a conflict-free personal schedule. The thing that must never happen is two
sessions in the same time slot. You could write "never pick two sessions in the same slot" into the
assemble prompt — and the code does, as a hint — but a prompt instruction is a *request*, not a
guarantee. A model that occasionally clashes anyway will hand back a broken schedule, and nothing
will have stopped it. The point of a framework-enforced guardrail is to remove the word "hope" from
that sentence.

## Why a postcondition alone does not gate the goal

Here is the trap. You might reasonably write the invariant as a postcondition on the goal:

```java
@AchievesGoal
@Action(post = {"noDoubleBooking"})   // looks like it enforces. It does not.
PersonalSchedule assembleSchedule(...) { ... }
```

In Embabel 0.5.0, `@Action(post = {...})` on an `@AchievesGoal` action is a **planning promise**, not
a runtime gate. The goal is considered *achieved the moment the action produces its output type* —
whether or not the post-condition actually holds. The planner used `post` to decide *whether to
schedule the action*; it does not re-evaluate it afterward to decide the goal is satisfied. So a
clashing `PersonalSchedule` is produced, the output type exists, and the goal is declared done. The
guardrail never bit.

This is not a bug to route around; it is a property to design with. A `post` describes what an action
*intends to bring about* so the planner can chain actions. Gating the *goal* is a different question,
and it needs a different mechanism.

## The fix: make the goal *pre*-require the invariant (assemble → confirm)

The pattern is to split the final step in two, so that the invariant becomes a *precondition* the
goal genuinely depends on:

```java
// 1. Producer: makes a draft, promises (post) it is clash-free, and may be re-run.
@Action(pre = {"hasCandidates"}, post = {"noDoubleBooking"}, canRerun = true)
DraftSchedule assembleSchedule(AttendeeProfile profile, ResearchedSessions researched, Ai ai) { ... }

// 2. The condition: side-effect-free, reads the draft, returns a boolean.
@Condition(name = "noDoubleBooking")
boolean noDoubleBooking(DraftSchedule draft) {
    var slots = draft.items().stream().map(ScheduleItem::slot).toList();
    return slots.size() == new HashSet<>(slots).size();
}

// 3. The goal: REQUIRES the invariant as a precondition.
@AchievesGoal(description = "Produce a conflict-free personal schedule")
@Action(pre = {"noDoubleBooking"})
PersonalSchedule confirmSchedule(DraftSchedule draft) { ... }
```

Now the chain has a real gate. `confirmSchedule` is the goal, and it *cannot run* unless
`noDoubleBooking` is true. If `assembleSchedule` produces a clashing draft, the condition is false,
`confirmSchedule`'s precondition is unmet, and the planner — because `assembleSchedule` is
`canRerun = true` — runs assembly *again*, looking for a clash-free arrangement. If none exists, it
does not return a broken schedule; it stops at the **budget**. The goal is conflict-free *by
construction*, because the only path to it passes through the precondition.

The mantra: **to make an invariant bite, the goal must require it as a `pre`, not merely promise it
as a `post`.** Embabel's own Researcher example uses this same draft-then-confirm shape; ConfPlanner
mirrors it. This is the [GOAP](goap.md) machinery doing exactly what it should — the planner gates on
availability of a true condition — once you point it at the right question.

## The other three guardrail shapes

The double-booking invariant is the deep one, but it sits in a family of four, each reassessed at a
different moment:

- **Precondition (need candidates).** `hasCandidates` is a `@Condition` that `assembleSchedule`
  *pre*-requires; it is unreachable unless some action *produces* it, which is why
  `shortlistSessions` carries `post = {"hasCandidates"}`. A condition with no producer can never
  become true, so the action that needs it can never run — a precondition is half of a
  produces/consumes pair. Reassessed before the action runs.
- **Budget.** `ConfPlannerShell` caps every run with `new Budget(0.50, 20, 200_000)` — dollars,
  actions, tokens — via `ProcessOptions`. The budget is what turns "re-run assembly until it works"
  into "re-run a bounded number of times, then stop visibly." Without it, the assemble → confirm loop
  could spin. Reassessed continuously. (Right-sizing the work *under* that cap is
  [its own subject](right-sizing-models.md).)
- **Secured tool.** `@SecureAgentTool("hasAuthority('conf:premium')")` gates the `premiumBriefing`
  action with a Spring-Security authority expression; an unauthorised caller is denied *before any
  LLM spend*. Note the design choice: `PremiumBriefing` is a side type the schedule goal never
  consumes, so adding access control does not touch the free flow. (This annotation needs Spring MVC;
  the default shell excludes its auto-config so the no-Docker path still boots — a setup detail, in
  the [reference](../reference/index.md).)

## Why guardrails live in the framework, not the prompt

The thread joining all four is that they are *framework-enforced*, not prompt-instructed. A prompt
that says "please don't" is advice; a precondition the goal depends on is a wall. ConfPlanner's
build-time harness states the same value as a constraint — *an invariant the goal must hold is a
`@Condition` the goal action pre-requires, and every guardrail has a test that proves it bites*. The
runtime guarantee and the build-time rule are the same idea at two altitudes; see
[the dual harness](the-dual-harness.md).

To *build* these four guardrails yourself, work Lab 3 in the [tutorials](../tutorials/index.md); to
*diagnose* a plan that goes `STUCK` because an invariant never holds, work Lab 4. For the exact
annotation members (`pre`, `post`, `canRerun`, `@Condition`, `Budget`, `@SecureAgentTool`), see the
[reference](../reference/index.md).
