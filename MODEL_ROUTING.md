# Model Routing

How ConfPlanner routes each action to a model, and why. Routing is a **configuration** decision:
code calls `withLlmByRole("cheapest"|"best")`, and `application.yml` maps roles to models. Swap the
models for whatever you have — including a local Ollama tag where data must stay in the building.

> For the step-by-step recipe (route an action, map the role, keep this table in step), see the
> how-to guide **[Route models by role](docs/how-to/route-models-by-role.md)**.

## Agent Routing Table

| Action | Role | Why (return-type complexity) | Default model |
|---|---|---|---|
| `extractAttendeeProfile` | `cheapest` | Pull a few fields from one sentence | `gpt-4.1-nano` |
| `loadCatalog` | _(none — plain code)_ | Deterministic JSON load, no LLM | — |
| `shortlistSessions` | `cheapest` | Match tags to a menu of options | `gpt-4.1-nano` |
| `researchSessions` | `best` | Reason about relevance + score each pick | `gpt-4.1` |
| `assembleSchedule` | `best` | Synthesise a conflict-free schedule + rationale | `gpt-4.1` |
| `confirmSchedule` | _(none — plain code)_ | Repackages a validated draft | — |
| `planNetworking` | `default` (`withDefaultLlm()`) | Reason over speakers to suggest people | `gpt-4.1-mini` |
| `premiumBriefing` | `default` | Short prose summary (secured tool) | `gpt-4.1-mini` |

Rule of thumb: if the return type is a flat list of strings, route `cheapest`; if it carries
judgement (scores, a rationale, a conflict-free arrangement), route `best`.

## Token Budget Guidance

Per-run budget is capped in `ConfPlannerShell` via `ProcessOptions` (`Budget(0.50, 20, 200_000)`).
Indicative shares of a single schedule run:

| Stage | Share of tokens | Notes |
|---|---|---|
| extract + shortlist (cheapest) | ~15% | Many tokens in (catalog menu), few out |
| research (best) | ~35% | One reasoned insight per shortlisted session |
| assemble (best) | ~45% | The bulk: reasoning + rationale |
| confirm (none) | ~0% | Plain code |

Levers:
- Route a step to a **local** model (Ollama/Docker, under a Spring profile) when its data cannot
  leave the building — trades latency/quality for data residency.
- Tighten the `Budget` to fail fast in CI or demos.
- Measure before optimising: run with all-`best` vs routed and compare the budget line in the log.
  Embabel 0.4.0+ also reports **cost per LLM call** (aggregated across subprocesses) — read the
  comparison in dollars per action, not just tokens.
- **Prompt caching** (0.4.0+, Anthropic): repeated context such as the catalog menu can be cached
  at the provider. Routing picks the model; the budget caps the run; caching cuts the price of
  what's left.

## Observed cost per action

Fill this in from the per-call cost lines in the planning log after a routed run and an all-`best`
run of the same request — the routing decision should be *measured*, not assumed. Re-measure when
models or the catalog change.

| Action | Routed cost (USD) | All-`best` cost (USD) | Notes |
|---|---|---|---|
| `extractAttendeeProfile` | _measure_ | _measure_ | |
| `shortlistSessions` | _measure_ | _measure_ | catalog menu dominates input tokens |
| `researchSessions` | _measure_ | _measure_ | |
| `assembleSchedule` | _measure_ | _measure_ | |
| **Total per run** | _measure_ | _measure_ | |
