# M3 — Guardrails: make the contract explicit — topic notes & research

> Slot: 11:50–12:50 (20 min frame + demo, 35 min lab, 5 min debrief). Lunch after.
> Branches: `lab3-before` → `lab3-after`. Habit: **Make the contract explicit.**

## The job of this module

This is the deepest technical lesson of the day and the one most worth over-rehearsing:
**a postcondition on the goal action is a planning promise, not a runtime gate.** The goal is
"achieved" the moment its output type exists. To make an invariant bite, the *goal must require it
as a `pre`* — hence the assemble → confirm split. If attendees take one mechanism home, it's this.

## Slide beats

1. **Hope vs. guarantee.** The assemble prompt already says "never pick two sessions in the same
   slot." Run the numbers from M0: a request honoured 99% of the time across thousands of runs is
   a steady stream of broken schedules. A guardrail's job is to delete the word "hope."
2. **The four shapes table** (from the worksheet): invariant / precondition / budget / access
   control — each with its mechanism and *when it is reassessed*. The reassessed-when column is
   the underrated one; it's what makes these guardrails rather than assertions.
   Then **the fifth shape** (0.5.0 content, worked example on `main`): a *content* guardrail —
   `RequestContentGuardRail implements UserInputGuardRail`, attached with `.withGuardRails(...)`
   where raw input first meets a model; screens prompt-injection markers deterministically,
   before any spend. Shapes 1–4 guard the plan; this one guards the content.
   `RequestContentGuardRailTest` proves it bites with no keys.
3. **The trap slide.** Show the natural-but-wrong code:
   `@AchievesGoal @Action(post = {"noDoubleBooking"}) PersonalSchedule assembleSchedule(...)` —
   "looks like it enforces. It does not." Explain *why* it's not a bug: `post` exists so the
   planner can chain actions; gating the goal is a different question needing a different
   mechanism. This distinction — planner beliefs vs. runtime checks — is the module.
4. **The fix diagram.** assemble (produces `DraftSchedule`, `post=noDoubleBooking`, `canRerun`) →
   condition (side-effect-free boolean over the draft) → confirm (`@AchievesGoal`,
   `pre=noDoubleBooking`). Walk the failure path: clashing draft → condition false → confirm
   blocked → planner re-runs assembly → budget stops it *visibly*. "Conflict-free by construction."
5. **Budget as a guardrail, not a bill.** `Budget(0.50, 20, 200_000)` — dollars, actions, tokens.
   Without it, assemble→confirm could spin forever; the budget converts an infinite retry into a
   bounded, visible stop. (Also the floor under Lab 6's routing decisions.)
6. **Access control before spend.** `@SecureAgentTool("hasAuthority('conf:premium')")` — a
   Spring Security SpEL expression on an action; unauthorised callers are denied *before any LLM
   spend*. Design note worth a bullet: `PremiumBriefing` is a side type the schedule goal never
   consumes, so securing it didn't touch the free flow.
7. **Keystone table callback** — row 3.

## Live demo script

```bash
git checkout lab3-after
./mvnw -q test -Dtest=GuardrailEnforcementTest   # the clash is refused: invoke throws, budget hit
./mvnw spring-boot:run
x "…" -p -r
```

Point at: `hasCandidates` flipping TRUE after shortlist; the draft appearing; `noDoubleBooking`
TRUE; confirm firing. Then narrate the counterfactual with the Lab 4 teaser: "hold that thought —
after lunch you'll watch a run where that condition *never* goes TRUE."

## Research & references

- **Design by Contract.** Bertrand Meyer, *"Applying 'Design by Contract'"* (IEEE Computer, 1992)
  and *Object-Oriented Software Construction*: preconditions, postconditions, invariants as
  first-class, machine-checked artifacts. Embabel's `pre`/`post`/`@Condition` is DbC ported to
  plan-space — with the twist that here `post` informs the *planner*, and only `pre` on the goal
  is a hard gate. That twist is exactly why the trap slide exists.
- **Hoare logic** (C.A.R. Hoare, 1969, {P} C {Q}) — one bullet of lineage for the formally minded;
  the world-state lines in the planning log are a Hoare-style state annotation you can actually
  read at runtime.
- **Guardrails as the industry gap.** OWASP *Top 10 for LLM Applications* — LLM01 prompt injection
  and **LLM08 excessive agency** are both answered structurally here: injection can't produce a
  double-booking (the condition is code, not a prompt), and agency is bounded by budget + secured
  tools. NIST AI RMF and the EU AI Act's human-oversight/robustness articles ask for exactly
  "demonstrable constraints"; a `@Condition` with a test *is* demonstrable.
- **Bounded retries / circuit-breaker thinking.** Nygard, *Release It!* (2007): fail fast, make
  failure visible, cap retry storms. `canRerun + Budget` is the agent-era circuit breaker — the
  budget line in the planning log is the "breaker opened" signal.
- **The empty-schedule guardrail.** `hasCandidates` is the quieter lesson: a precondition is *half
  of a produces/consumes pair* — some action must `post` it or nothing can ever run. Conditions
  are part of the plan graph, not decorations. (This becomes the Lab 4 fault taxonomy.)
- **Tests that prove the guardrail bites.** `GuardrailEnforcementTest` asserts the *refusal* — the
  invoke throws rather than returning a clash. Cite Feathers' seams (M7 material): the mocked-LLM
  boundary is what lets you unit-test "the framework refuses" without keys.

## Track B foil — predict out loud

Ask an ungoverned agent to "stop double-booking" and it will almost certainly strengthen the
prompt ("IMPORTANT: never…") — hope with more capital letters. Some will add a post-hoc `if` that
silently drops a session — which *hides* the violation instead of surfacing it. The harnessed
answer is the reassessed condition the goal depends on, plus the budget so refusal terminates.

## Debrief prompts

- "Who wrote the postcondition-only version first?" Normalise it — the trap is natural; that's why
  it's the lesson.
- Map to their systems: what's your `noDoubleBooking`? (Refunds ≤ payment; meds don't interact;
  trade within limits.) Every domain has one invariant the goal must *require*.

## Transition out

"You've built an agent that would rather stop than lie. After lunch: what it looks like when it
stops — and how to read *why* from the log instead of guessing."
