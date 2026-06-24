# Configuration

ConfPlanner is configured through `src/main/resources/application.yml`, Spring profiles, Maven
profiles, and `.env`. The per-run budget is set in code via `ProcessOptions`. To enable a provider
or tracing, see the how-to guides ([real model](../how-to/run-with-a-real-model.md),
[mock mode](../how-to/use-mock-mode.md), [Zipkin tracing](../how-to/enable-zipkin-tracing.md)).

## application.yml keys

| Key | Default | Description |
|---|---|---|
| `embabel.models.default-llm` | `gpt-4.1-mini` | Model used by `withDefaultLlm()`. |
| `embabel.models.llms.cheapest` | `gpt-4.1-nano` | Model for the `cheapest` role (`withLlmByRole("cheapest")`). |
| `embabel.models.llms.best` | `gpt-4.1` | Model for the `best` role (`withLlmByRole("best")`). |
| `embabel.agent.logging.personality` | `default` | Logging personality. `starwars` is an alternative. |
| `spring.application.name` | `confplanner` | Application name. |
| `spring.shell.interactive.enabled` | `true` | Enables the interactive Embabel shell. |
| `spring.autoconfigure.exclude` | `com.embabel.agent.autoconfigure.mcpserver.security.AgentMcpServerAutoConfiguration` | Excludes the MCP web-security auto-config so the shell boots without Spring MVC. |

Don't hard-code model names in Java; models change between Embabel releases. Route by role and edit
the role→model mapping here.

## Spring profiles

Activated with `--spring.profiles.active=<name>` or `-Dspring-boot.run.profiles=<name>`.

| Profile | Sets | Effect |
|---|---|---|
| `mock` | `embabel.agent.platform.test.mock-mode: true` | Deterministic runs with no model calls and no keys. |
| `observability` | `embabel.agent.platform.observability.enabled: true`, `embabel.agent.platform.observability.service-name: confplanner`, `management.tracing.enabled: true`, `management.tracing.sampling.probability: 1.0`, `management.zipkin.tracing.endpoint: http://127.0.0.1:9411/api/v2/spans` | Exports traces to a local Zipkin collector. Pair with the `observability` Maven profile. |

No profile is active by default.

## Maven profiles

In `pom.xml`. Provider profiles auto-activate on the matching environment variable.

| Profile | Activation | Adds dependency |
|---|---|---|
| `openai-models` | env `OPENAI_API_KEY` present | `embabel-agent-starter-openai` |
| `anthropic-models` | env `ANTHROPIC_API_KEY` present | `embabel-agent-starter-anthropic` |
| `observability` | manual (`-Pobservability`) | `embabel-agent-starter-observability`, `opentelemetry-exporter-zipkin` |

Pinned versions: `embabel-agent.version` = `0.5.0`, `java.version` = `21`, Spring Boot parent =
`3.5.13`.

## Environment variables (.env)

`.env` is git-ignored; copy `.env.example` to `.env` and set one provider key. Keys are only needed
to run the agent against a real model — the build and tests are green without any key.

| Variable | Description |
|---|---|
| `OPENAI_API_KEY` | OpenAI provider key. Activates the `openai-models` Maven profile. Default provider. |
| `ANTHROPIC_API_KEY` | Anthropic provider key. Activates the `anthropic-models` Maven profile. |

## ProcessOptions budget

Set in `ConfPlannerShell` (and in the Lab 3 worksheet) on the `AgentInvocation`. The planner stops
early rather than exceeding the cap, and the stop is visible in the planning log
(`MaxActionsEarlyTerminationPolicy`).

```java
var budget = new Budget(0.50, 20, 200_000);
var options = ProcessOptions.DEFAULT.withBudget(budget);
```

`com.embabel.agent.core.Budget` parameters, in declaration order:

| Parameter | Value | Unit | Meaning |
|---|---|---|---|
| cost | `0.50` | USD | Maximum spend per run. |
| actions | `20` | count | Maximum action executions per run. |
| tokens | `200_000` | tokens | Maximum token budget per run. |
