# Lab 5 — Extend without breaking (capstone)

> **Goal:** add a networking plan to the conference assistant without changing the schedule flow.
> **Objective:** new capability arrives as new types/actions (or a new agent), never as a rewire.
> **Habit:** **Extend by adding, not rewiring.**
> **Ladder rung:** Track C preferred (Orchestrating → Supervising).
> **Branches:** `lab5-before` → `lab5-after`.

## Why this lab

The test of a well-modelled agent is how it grows. A networking feature should not require you to
touch `assembleSchedule`, `confirmSchedule`, or any guardrail — if it does, the seams were in the
wrong place. Because each capability is a typed action, you add behaviour by adding a producer of a
new type and letting the planner route to it. Here we go one step further and add the capability as
a **separate agent**, so the existing `ConfPlannerAgent` (and every regression test on it) is
provably untouched.

> **Two 0.5.0 constraints shape the design** (both discovered by reading the planning log):
> 1. An `@Agent` may declare only **one goal type**. `PersonalSchedule` is already
>    `ConfPlannerAgent`'s goal, so `NetworkingPlan` needs its own agent.
> 2. An agent plans **only with its own actions** — it cannot borrow another agent's or an
>    `@EmbabelComponent`'s actions. So the new agent must declare the upstream steps it needs.
>
> The way to avoid copy-pasted logic is to put the pipeline *logic* in a plain `@Service` and let
> each agent declare thin `@Action` wrappers that delegate to it. Shared logic, per-agent actions.

## Before state (`lab5-before`)

- No networking capability. A `// TODO (Lab 5)` anchor in `ConfPlannerAgent` points at the new agent.

## Steps

1. **Add the domain record:** `NetworkingPlan(List<String> peopleToMeet, String rationale)`
   (implement `HasContent` for a nice render).
2. **Extract the shared pipeline into a `@Service`** `ConfPlanningCapabilities` with plain methods
   `extractProfile`, `catalog`, `shortlist`, `research` (move the prompt bodies here). This is the
   only edit to the schedule path, and it leaves the schedule's *behaviour* identical.
3. **Make `ConfPlannerAgent` delegate:** its `@Action` methods become one-line wrappers calling the
   service. Same actions, same plan, same output — the regression test proves it.
4. **Add `ConfNetworkingAgent`:** a new `@Agent` whose single goal is `NetworkingPlan`. It declares
   its *own* `extract/loadCatalog/shortlist/research` wrappers (also delegating to the service) plus
   `@AchievesGoal planNetworking(AttendeeProfile, ResearchedSessions, Ai)`. Suggest people from the
   researched sessions' speakers.
5. Build: `./mvnw -q verify`.

## Acceptance check (framework-enforced)

- Invoking the `NetworkingPlan` goal returns networking suggestions; its planning log shows
  `extract → loadCatalog → shortlist → research → planNetworking` — the existing pipeline reused.
- **Regression:** the `PersonalSchedule` flow plans and runs unchanged — its integration test (and
  `GuardrailEnforcementTest`) stay green. The only change to the schedule path is moving prompt
  bodies into a service; the plan and output are identical.
- If done via Track C, the two-stage review passed before the change landed.

## Three-track notes

- **Track A:** add the record and agent by hand; invoke both goals; confirm the schedule test is
  still green.
- **Track B:** ask an ungoverned agent to "add networking." Did it edit `assembleSchedule` or bolt
  networking onto the schedule goal (rewiring), or add an independent producer (extending)?
- **Track C (preferred):** drive it through the harness. The orchestrator plans the change, the
  `java-implementer` adds the new files under `src/main/java/**`, and the constraint "capability is
  added via new types/actions only; never hard-wire flow order" is exactly what is being graded.
  Read the plan and the trace; confirm the regression.

## Going further

- Have `planNetworking` also read the secured `PremiumBriefing` when the caller is authorised,
  composing Lab 3's access control with the new capability.
- Surface both goals from one shell command that invokes `PersonalSchedule` then `NetworkingPlan`.
