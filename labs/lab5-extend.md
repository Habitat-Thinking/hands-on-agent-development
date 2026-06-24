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

> **0.5.0 note:** an Embabel `@Agent` may declare only one goal type. `PersonalSchedule` is already
> `ConfPlannerAgent`'s goal, so `NetworkingPlan` must live in its own agent. This is a feature, not
> a workaround: a new agent is the strongest possible "extend by adding."

## Before state (`lab5-before`)

- No networking capability. A `// TODO (Lab 5)` anchor in `ConfPlannerAgent` points at the new agent.

## Steps

1. **Add the domain record:** `NetworkingPlan(List<String> peopleToMeet, String rationale)`.
2. **Add a new agent** `ConfNetworkingAgent`:
   ```java
   @Agent(description = "Suggest who an attendee should meet at the conference")
   public class ConfNetworkingAgent {
       @AchievesGoal(description = "Produce a networking plan")
       @Action
       NetworkingPlan planNetworking(AttendeeProfile profile, ResearchedSessions researched, Ai ai) { ... }
   }
   ```
   It consumes `AttendeeProfile` and `ResearchedSessions` — types the existing pipeline already
   produces — so invoking the `NetworkingPlan` goal plans straight through
   `extract → loadCatalog → shortlist → research → planNetworking`. Suggest people from the
   researched sessions' speakers.
3. Build: `./mvnw -q verify`.

## Acceptance check (framework-enforced)

- Invoking the `NetworkingPlan` goal returns networking suggestions.
- **Regression:** the original `PersonalSchedule` flow plans and runs unchanged — its integration
  test (and `GuardrailEnforcementTest`) stay green. `ConfPlannerAgent` has no new edits beyond the
  anchor comment.
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
