# CLI

ConfPlanner runs as an interactive Embabel shell (Spring Shell). This page lists the commands used
in the workshop. To run the agent for the first time, see
[Run against a real model](../how-to/run-with-a-real-model.md); for a key-free run, see
[mock mode](../how-to/use-mock-mode.md).

## Running the app

| Command | Effect |
|---|---|
| `./mvnw clean verify` | Compile, run unit + Mockito integration tests. Green with no API keys. |
| `./mvnw spring-boot:run` | Start the Embabel shell. |
| `SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run` | Start the shell in mock mode (no model calls). |
| `SPRING_PROFILES_ACTIVE=observability ./mvnw -Pobservability spring-boot:run` | Start the shell with Zipkin tracing enabled. |

Requires Java 21 and the bundled Maven wrapper (`./mvnw`); no system Maven is needed.

## Shell commands

| Command | Alias | Description |
|---|---|---|
| `x "<request>"` | `execute "<request>"` | Match the free-text request to a goal and run the plan. |
| `plan "<request>"` | — | Invoke the `PersonalSchedule` goal directly via `AgentInvocation` (provided by `ConfPlannerShell`). Bypasses the LLM goal-ranker, so it is the **key-free / mock-mode** invocation; prints the planning log by default and takes no verbosity flags. |

`x` / `execute` let the platform match the request to a registered goal. `plan` is a custom
`@ShellMethod` that calls a specific goal programmatically; its `request` argument defaults to
`"I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"`.

## Verbosity flags

Appended to `x` / `execute` to surface the planning detail. They are **not** available on `plan`, and
`x` needs a live model to rank the request — for a key-free run use `plan`, which prints the planning
log by default.

| Flag | Effect |
|---|---|
| `-p` | Show prompt verbosity — the prompts sent to the model, including `PromptContributor` contributions. |
| `-r` | Show result/response verbosity — the model's responses. |

Example:

```
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule" -p -r
```

The planning log printed by `-p -r` shows, cycle by cycle, what the planner believed (world-state
conditions) and which action it chose — the same world-state a Zipkin trace shows. A budget stop
appears as `MaxActionsEarlyTerminationPolicy`.
