# How-to guides

Task-oriented recipes for getting a specific job done in ConfPlanner. Each guide assumes you already
know what you want and shows the steps to achieve it. If you are starting from zero, work the
[tutorials](../tutorials/index.md) first; to look up a flag, role name, or annotation, jump to the
[reference](../reference/index.md); for the *why* behind a design, see the
[explanation](../explanation/index.md).

## Run and operate

- **[Run ConfPlanner against a real model](run-with-a-real-model.md)** — set a provider key in
  `.env`, start the shell, and invoke a goal.
- **[Run a deterministic demo with mock mode](use-mock-mode.md)** — the `mock` Spring profile for
  key-free, token-free, repeatable runs.
- **[Enable Zipkin tracing](enable-zipkin-tracing.md)** — bring up the collector, run with the
  observability profiles, and read the trace at port 9411 (plus the no-Docker fallback).

## Change the agent

- **[Add a new typed action](add-an-action.md)** — drop an `@Action` into the pipeline and let GOAP
  route through it (the Lab 2 shape).
- **[Add a guardrail](add-a-guardrail.md)** — an invariant the goal pre-requires, a precondition, and
  a budget (the Lab 3 shape).
- **[Route models by role](route-models-by-role.md)** — `withLlmByRole` plus `application.yml`, and
  keep `MODEL_ROUTING.md` in step.
- **[Route a step to a local model](route-a-step-to-a-local-model.md)** — keep a step's data in the
  building: bind a role to a local (Ollama) model under a Spring profile, key-free (the Lab 6
  regulated-environment lever).

## Drive it through the harness

- **[Drive a change with the build-time harness](drive-a-change-with-the-harness.md)** — Track C:
  orchestrator → `java-implementer` → two-stage review.
