# Run the eval lane

The keyless build (`./mvnw verify`) mocks the model and proves the **scaffolding** — the plan
derives, the guardrails gate, the budget holds. It says nothing about whether a schedule is actually
*good*: relevant to the attendee, balanced, the picks a human expert would make. That is a different
question — the model's *judgement*, which is non-deterministic and needs a real model to test.

The **eval lane** answers it, for a small golden set, with an **LLM-as-judge**. It is tagged
`@Tag("eval")` and **excluded from the default `verify` gate**, so the keyless build stays green and
deterministic. You run it deliberately, with a key.

## What it does

For each case in the golden set (`ScheduleQualityEvalTest.goldenCases()`), the lane:

1. runs the **real** agent against a **real** model for that request;
2. applies **deterministic gates** as a belt — no two sessions share a slot, every session is from
   the catalog, the item count is sane, and any topic the case says to avoid is absent;
3. asks the strong model, via the `ScheduleJudge` agent, for a typed `ScheduleVerdict` — `relevance`
   and `balance` (1–5) and an `onProfile` boolean — and asserts each clears the bar (relevance ≥ 3,
   balance ≥ 3, on-profile true).

Deterministic gates for the seams; a sampled judge for the judgement — the two halves of a real
agent test strategy.

## Run it

Provide a provider key and select the `eval` profile (it runs **only** `@Tag("eval")` tests):

```bash
OPENAI_API_KEY=sk-... ./mvnw -Peval test
```

A key in `.env` also works (it is loaded into the Spring `Environment`). Without any key the cases
**skip** — reported as skipped, not failed — because the live assertions are guarded by a JUnit
assumption; the Spring context still boots, so the wiring stays honest even key-free:

```bash
./mvnw -Peval test        # no key → "Tests run: 3, Skipped: 3"
```

The default build is unaffected and needs nothing:

```bash
./mvnw -q verify          # green, no key — the eval lane is excluded here
```

## Read a failure

When a case fails, the assertion message carries the judge's own words — e.g.
`relevance 2/5 below bar — the schedule leans entirely on Kubernetes and ignores the stated DevEx
and resilience interests`. That rationale *is* the signal: it tells you whether the agent's
judgement slipped, in a sentence, the same way the planning log tells you whether the scaffolding
slipped. Read it, don't re-run and hope.

## Extend the golden set

Add a case to `goldenCases()` in `ScheduleQualityEvalTest`: a request, the tags it must never
contain (e.g. `List.of("vendor")` for "no vendor keynotes"), and the item-count band. Keep the set
small and representative — an eval lane is a *sample*, not a proof, and every case costs a real run.
The judge rubric lives in `ScheduleJudge`; tighten it (or the pass thresholds) as your quality bar
rises.

## Where this fits

This is the honest complement the workshop's own scope audit calls for
(`slides/notes/gaps-and-extensions.md`, Gap 10): run-time "trust" as taught in the six labs is
*legibility plus bounded invariants*, sufficient while a human reads each plan. At the
**govern-the-loop** horizon (habit 8, and Lab 7's agentic RAG), where no human reads every run, the
judgement has to be checked automatically — and that is exactly what this lane samples.

---

For the deterministic side of the strategy — mocking the model at the action boundary — see
[About the runtime harness](../explanation/the-runtime-harness.md) and habit 7 in
[The eight habits](../explanation/the-eight-habits.md). For routing the judge (it uses the `best`
role) see [Route models by role](route-models-by-role.md).
