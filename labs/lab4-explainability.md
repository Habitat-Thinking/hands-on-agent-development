# Lab 4 — Explainability: debug by reading state

> **Goal:** find the root cause of a stuck agent from the planning log and the trace — *before*
> touching the code.
> **Objective:** an agent you can read is an agent you can trust and fix.
> **Habit:** **Read the plan, not the vibes.**
> **Ladder rung:** Track A → C (you must be able to read state by hand first).
> **Branches:** `lab4-broken` → `lab4-after`.

## Why this lab

When an LLM app misbehaves the temptation is to poke the prompt and re-run. Embabel gives you
something better: a **planning log** (what the planner believed and chose, cycle by cycle) and a
**trace** (what actually executed). A stuck agent is a *legible* failure — the world-state tells you
exactly which condition stayed false.

## Before state (`lab4-broken`)

One deliberate fault: `assembleSchedule` pins every `ScheduleItem` to the placeholder slot `"TBD"`
instead of the session's real slot. With more than one session that is always a double-booking, so
`noDoubleBooking` never holds, `confirmSchedule`'s precondition is never met, and the agent goes
`STUCK`, re-running assembly until the budget stops it. **The code compiles**; it fails at runtime.

> Note: this branch is the one place in the repo where `./mvnw verify` is *expected* to fail — the
> failing integration test is the thing you are here to diagnose.

## Steps — diagnose first, fix second

1. **Reproduce.** Run `./mvnw test` (or the agent) and read the failure: the goal is never
   achieved; the process ends `STUCK` after hitting `MaxActionsEarlyTerminationPolicy`.
2. **Read the planning log:**
   ```
   x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
   ```
   In the world-state lines, find the condition that never flips to `TRUE`. You are looking for
   `noDoubleBooking` staying `FALSE` after `assembleSchedule` runs, so `confirmSchedule` never fires.
3. **(Optional) Read the trace in Zipkin:**
   ```
   docker compose up -d            # starts Zipkin on 9411
   ./mvnw spring-boot:run -Dspring-boot.run.profiles=observability
   ```
   Open <http://127.0.0.1:9411>, find the run, and watch `assembleSchedule` execute repeatedly
   while the goal span never closes. (Needs Docker — the planning-log path above does not.)
4. **State the root cause in one sentence** before editing: *"assembleSchedule puts every item in
   one slot, so noDoubleBooking is always false and confirmSchedule can never run."*
5. **Fix it** (see `lab4-after`): place each item in its session's real slot —
   `new ScheduleItem(s, s.slot())`.
6. **Prove it.** On `lab4-after` the regression test shows the plan completes and the schedule
   satisfies `noDoubleBooking`.

## Acceptance check (framework-enforced)

- You identify the failed condition (`noDoubleBooking`) from the log/trace **before** changing code.
- After the fix, the goal span completes and the regression test is green.

## Three-track notes

- **Track A:** read the planning log by eye. This is the ground truth — do it without an agent once.
- **Track B:** paste the stuck log into an ungoverned agent and let it guess. Compare the guess to
  what the world-state actually says. The log is evidence; the guess is a vibe.
- **Track C:** the harness's two-stage review would have caught a change that breaks an invariant —
  the trace makes the regression obvious in review.

## Going further

- Inject a *different* fault (give `researchSessions` a return type `assembleSchedule` does not
  consume) and confirm the failure's *shape* changes: a severed produces/consumes link shows up as
  a type that is never `TRUE`, versus a condition that flips false.
