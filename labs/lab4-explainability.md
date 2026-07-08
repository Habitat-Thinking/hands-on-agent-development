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

1. **Reproduce.** Run `./mvnw test` (no key needed — the LLM is mocked) and read the failure. You'll
   see `assembleSchedule` execute over and over, then
   `early termination by MaxActionsEarlyTerminationPolicy(maxActions=50) … error=true`, and the
   integration test fail with a `NullPointerException` out of `invoke` — the goal was never produced.
   Note: the literal word `STUCK` shows up only in the `[flight-recorder]` summary line
   (`PlanFlightRecorder`), not in the failing test's own output, so grep for
   `MaxActionsEarlyTerminationPolicy` — that is the real signature of a stalled plan.
2. **Read the planning log.** The `x` command needs a model, so use a key **or** the keyless mock
   profile:
   ```
   SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run
   x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
   ```
   (No key and don't want the shell? The same world-state is already printed by the `./mvnw test`
   run in Step 1.) In the world-state lines, find the condition that never flips to `TRUE`. You are
   looking for `noDoubleBooking` staying `FALSE` after `assembleSchedule` runs, so `confirmSchedule`
   never fires.
3. **(Optional) Read the trace in Zipkin:**
   ```
   docker compose up -d            # starts Zipkin on 9411
   SPRING_PROFILES_ACTIVE=observability ./mvnw -Pobservability spring-boot:run
   ```
   (The `-Pobservability` Maven profile puts the OpenTelemetry + Zipkin exporter on the classpath;
   without it Zipkin stays silently empty. Pass the Spring profile via the environment — the
   `-Dspring-boot.run.profiles=…` flag reaches Spring Shell as a command and fails with
   `CommandNotFound`.)
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
- **A third failure shape: exceptions.** Since 0.4.0 Embabel classifies action exceptions as
  retriable vs non-retriable. Make `researchSessions` throw a transient exception and re-run:
  the retry shows up in the log as its own signature — distinct from a condition that stays
  `FALSE` (this lab) and a type never produced (above). Three faults, three log shapes: that's a
  diagnosis table, not vibes.
- **Read the log in style.** Set `embabel.agent.logging.personality` in `application.yml` to one
  of `starwars | severance | hitchhiker | montypython | colossus` and re-run the broken branch. A
  stuck plan narrated by Vader is still a stuck plan — the world-state lines carry the same
  evidence. (Genuinely useful for demos: the personality makes the planning log impossible to
  ignore.)
- **Build your own black box.** The planning log and the trace are Embabel's observability
  surfaces; the third kind is one you *build*. Any Spring bean implementing
  `AgenticEventListener` (`com.embabel.agent.api.event`) is picked up by the platform and
  receives every process event — plans formulated (with the `WorldState` and `Plan`), action
  results (with durations), goals achieved, `STUCK`. `main` carries a worked example:
  `PlanFlightRecorder` keeps per-process counters and emits one `[flight-recorder]` summary
  line per run — *replans* are derived (every planning cycle beyond the first), so on
  `lab4-broken` that one number races upward while the goal never arrives: the whole diagnosis,
  one integer. It needs no Docker and no keys — watch it fire inside the mocked integration
  tests, and `PlanFlightRecorderTest` drives it with synthetic events. Exercise: extend it to
  also count `LlmRequestEvent`s per action, or forward the summary to the observability system
  you already run. (The logging personalities are implemented as exactly this kind of
  listener — you have been watching one all day.)
