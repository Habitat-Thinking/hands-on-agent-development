# Lab 2 — GOAP: add an action, watch the plan grow

> **Goal:** insert a research step between shortlist and assemble *without touching any flow order*.
> **Objective:** goal-oriented behaviour — name the goal, let the planner find the steps.
> **Habit:** **Name the goal, not the steps.**
> **Ladder rung:** Track B → C (Commanding → Regulating).
> **Branches:** `lab2-before` → `lab2-after`.

## Why this lab

Embabel uses GOAP (Goal-Oriented Action Planning). You never write the sequence; you declare what
each action *consumes* and *produces*, and the planner derives the order to reach the goal. The
proof is that you can drop a new action into the middle and the plan re-routes through it on its
own — because the new action produces what a downstream action now needs.

## Before state (`lab2-before`)

- `assembleSchedule` consumes `CandidateSessions` directly.
- A `// TODO (Lab 2)` anchor marks where the research step goes.
- Inferred plan: `extract → loadCatalog → shortlist → assemble`.

## Steps

1. **Add two domain records:**
   - `SessionInsight(Session session, String whyRelevant, double matchScore)`
   - `ResearchedSessions(List<SessionInsight> insights)`
2. **Add the action:**
   ```java
   @Action
   ResearchedSessions researchSessions(CandidateSessions candidates, Ai ai) { ... }
   ```
   Have the model return, per shortlisted session, *why it is relevant* and a 0–1 match score.
   (Keep the id-only pattern: the model emits `sessionId` + `whyRelevant` + `matchScore`; code
   resolves ids back to `Session`.)
3. **Re-point the goal:** change `assembleSchedule` to consume `ResearchedSessions` instead of
   `CandidateSessions`. Do **not** reorder anything by hand.
4. Build and run with `x "..." -p -r`. Read the planning log.
5. **Replanning exercise:** temporarily `return null;` from `researchSessions` and re-run. Watch the
   planner mark the produces/consumes link unsatisfied and **replan** rather than crash. Put it back.

## Acceptance check (framework-enforced)

- The planning log now reads `… → shortlist → research → assemble`. The new action appears
  *between* the other two **with no flow rewiring** — you only changed types.
- You can explain the order purely from the types: `research` produces `ResearchedSessions`, which
  `assemble` now consumes, so `research` must run first.
- The integration test (regression) still reaches the `PersonalSchedule` goal.

## Three-track notes

- **Track A:** add the records and the action by hand; diff the planning log before/after.
- **Track B:** tell the agent "add a research step before assembling." Did it try to *call*
  research from inside assemble (hard-wiring), or add a typed action and let GOAP route it? The
  former is the anti-pattern this lab inoculates against.
- **Track C:** orchestrator dispatches `java-implementer`; the constraint "never hard-wire flow
  order; add capability via new types/actions only" is exactly what's being enforced here.

## Going further

- Give `researchSessions` web access: `ai.withDefaultLlm().withToolGroup(CoreToolGroups.WEB)…`
  (needs a web tool / Docker — see SETUP.md). The default lab path works without it.
- Add a second goal-irrelevant action that produces a type nothing consumes. Confirm the planner
  never schedules it — dead capability costs nothing because the plan is derived, not coded.
