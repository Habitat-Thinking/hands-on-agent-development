# Lab 6 — Model routing: the cheapest model that passes

> **Goal:** route each action to a model sized for its job — cheap for extraction, strong for synthesis.
> **Objective:** cost is a design parameter; justify the model per action by return-type complexity.
> **Habit:** **Right-size the model.**
> **Ladder rung:** Track C (Orchestrating).
> **Branches:** `lab6-before` → `lab6-after`.

## Why this lab

Not every step needs your best model. Pulling a few fields out of a sentence is cheap work;
synthesising a conflict-free schedule with a rationale is not. Embabel lets you choose per action
with `withLlmByRole("...")`, reading the role→model mapping from config — so routing is a
configuration decision, not a code rewrite. This is also the regulated-environment lever: route a
step to a **local** model when its data cannot leave the building.

## Before state (`lab6-before`)

- Every action uses `withDefaultLlm()` / `withAutoLlm()`. A `// TODO (Lab 6)` anchor marks the first
  one. `application.yml` already defines roles `cheapest` and `best`.

## Steps

1. **Route the cheap steps.** In `ConfPlanningCapabilities`, change `extractProfile` and `shortlist`
   to `ai.withLlmByRole("cheapest")`.
2. **Route the strong steps.** Change `research` (in the service) and `assembleSchedule` (in
   `ConfPlannerAgent`) to `ai.withLlmByRole("best")`.
3. **Make the config real.** Confirm `application.yml` maps the roles, e.g.:
   ```yaml
   embabel:
     models:
       default-llm: gpt-4.1-mini
       llms:
         cheapest: gpt-4.1-nano
         best: gpt-4.1
   ```
   Swap these for whatever you actually have — including a local Ollama tag for `cheapest`.
4. **Update `MODEL_ROUTING.md`** so its routing table matches the code.
5. Build: `./mvnw -q verify`. (Tests mock the LLM, so routing changes don't need keys to stay green.)

## Acceptance check

- At least one action is re-routed by role/config without touching its logic — only the
  `withLlmByRole(...)` call changed.
- `MODEL_ROUTING.md`'s table reflects the choices, and you can justify each by return-type
  complexity (a `List<String>` of tags vs a reasoned `PersonalSchedule`).

## Three-track notes

- **Track A:** flip the roles by hand; read the planning log to confirm which model each action used.
- **Track B:** ask an ungoverned agent to "make it cheaper." Did it hard-code a model name in Java
  (a rewrite) or move the choice to config (a routing decision)?
- **Track C:** the harness constraint "LLM choice is justified per action by return-type
  complexity" is exactly this lab. The orchestrator routes; you justify and record in `MODEL_ROUTING.md`.

## Going further

- Put one action behind a **local model** under a Spring profile (Ollama/Docker), so a regulated
  step keeps its data in the building. Note the trade-off in `MODEL_ROUTING.md`'s token-budget table.
- Measure: run the same request with all-`best` vs routed, and compare the token budget in the log.
