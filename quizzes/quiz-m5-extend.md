# Quiz — M5: Extend without breaking (Lab 5)

> Section: **Extend — the capstone**. Habit: **Extend by adding, not rewiring.**
> Four short questions. Attempt them before opening the answers.

## Questions

1. Why can't you just add a second `@AchievesGoal` for `NetworkingPlan` to the existing
   `ConfPlannerAgent`? Name the two Embabel 0.5.0 facts that force a new agent.
2. Given "an agent plans only with its own actions," how do you reuse the
   understand→load→shortlist→research pipeline *without copy-pasting it* into the new agent?
3. After the refactor, the diff on `ConfPlannerAgent` is **large** — every action becomes a one-liner.
   How do you know you didn't change its behaviour? (What kind of claim is "provably untouched"?)
4. Transfer: think of a feature your team shipped that ended up editing the middle of an existing
   flow. Was it genuinely a rewire, or an *add* that lacked a seam like this one?

<details>
<summary>Answers &amp; discussion</summary>

1. **(a)** An `@Agent` supports **one goal type**, and `PersonalSchedule` is already taken; **(b)** an
   agent plans **only with its own actions** — it can't borrow another agent's, and
   `@EmbabelComponent` actions aren't pooled into another agent's planning either. So `NetworkingPlan`
   needs its own agent.
2. **Lift the shared logic into a plain `@Service`** (`ConfPlanningCapabilities`); each agent keeps its
   own thin `@Action` wrappers that delegate to it. Shared logic, per-agent actions. (Same shape as
   the build-time harness: behaviour in one governed place, thin edges calling in.)
3. **"Provably untouched" is a *test* claim, not a *diff* claim.** The big diff is cosmetic (methods
   become one-liners); the proof that behaviour is identical is that the schedule integration test and
   `GuardrailEnforcementTest` still pass. The green suite — not a code review — says the flow is
   unchanged.
4. *(Your answer.)* The useful reflection: where a past "add" turned into a rewire because the shared
   logic had no `@Service`-shaped seam to hang off.

</details>
