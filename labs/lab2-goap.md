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
2. **Add the action — with an inner id-only output record.** Following the same idiom as
   `shortlistSessions`/`Shortlisting`, create an inner LLM-output record the model fills in, then
   resolve its ids back to real `Session`s:
   ```java
   // inner output records (id-only — what the model returns)
   record Insight(String sessionId, String whyRelevant, double matchScore) {}
   record ResearchOutput(List<Insight> insights) {}

   @Action
   ResearchedSessions researchSessions(CandidateSessions candidates, Ai ai) {
       // model → ResearchOutput (ids), then map each Insight to a SessionInsight (resolved Session)
   }
   ```
   Have the model return, per shortlisted session, *why it is relevant* and a 0–1 match score. The
   id-only `ResearchOutput`/`Insight` is the LLM's contract; `ResearchedSessions`/`SessionInsight`
   (step 1) is the resolved domain type the rest of the plan consumes.
3. **Re-point the goal:** change `assembleSchedule` to consume `ResearchedSessions` instead of
   `CandidateSessions`. Do **not** reorder anything by hand.
4. **Add the research stub to the integration test, then build** (`./mvnw -q verify`) — see the note
   below.
5. **(Live model — needs a key)** Run with `x "..." -p -r` and read the planning log.
6. **(Live model — needs a key) Replanning exercise:** temporarily `return null;` from
   `researchSessions` and re-run. Watch the planner mark the produces/consumes link unsatisfied and
   **replan** rather than crash. Put it back.

> **Your diff will also show…** the new `researchSessions` action runs inside the mocked
> integration test, so the test needs a stub for it or `./mvnw verify` fails with:
>
> ```
> NullPointerException: Cannot invoke "…ConfPlannerAgent$ResearchOutput.insights()"
>   because "output" is null
> ```
>
> Add a `whenCreateObject` matcher for the research step in `ConfPlannerAgentIntegrationTest`,
> alongside the existing shortlist and schedule stubs:
>
> ```java
> whenCreateObject(prompt -> prompt.contains("why it is relevant"),
>         ConfPlannerAgent.ResearchOutput.class)
>     .thenReturn(new ConfPlannerAgent.ResearchOutput(List.of(
>         new ConfPlannerAgent.Insight("PC-01", "core platform topic", 0.9),
>         new ConfPlannerAgent.Insight("PC-02", "golden paths", 0.8))));
> ```
>
> **Keyless acceptance:** this mocked integration test *is* the key-free proof — its console output
> prints the full planning log (`… → shortlistSessions → researchSessions → assembleSchedule → …`)
> during `./mvnw verify`, so you can confirm the new action landed in the plan without a live model.
> Steps 5–6 above only add the live view.

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
