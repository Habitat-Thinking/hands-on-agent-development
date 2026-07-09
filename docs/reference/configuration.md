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

Activated with the `SPRING_PROFILES_ACTIVE=<name>` environment variable, e.g.
`SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run`. Do **not** use
`-Dspring-boot.run.profiles=<name>`: the Boot plugin forwards it as a program argument, which Spring
Shell tries to execute as a command and rejects with `CommandNotFound`.

| Profile | Sets | Effect |
|---|---|---|
| `mock` | Routes every LLM role to the in-JVM stub — `embabel.models.default-llm: mock` and `llms.cheapest` / `llms.best: mock`. (It also sets `embabel.agent.platform.test.mock-mode: true`, but that flag is **inert** at 0.5.0 — no runtime class reads it; the role overrides are the real seam.) | Deterministic runs with no model calls and no keys. Invoke with `plan`, not `x`. |
| `observability` | `embabel.agent.platform.observability.enabled: true`, `embabel.agent.platform.observability.service-name: confplanner`, `management.tracing.enabled: true`, `management.tracing.sampling.probability: 1.0`, `management.zipkin.tracing.endpoint: http://127.0.0.1:9411/api/v2/spans` | Exports traces to a local Zipkin collector. Pair with the `observability` Maven profile. |

No profile is active by default.

## Providers on the classpath

Both provider starters (`embabel-agent-starter-openai`, `embabel-agent-starter-anthropic`) are
**always** on the classpath — plain `pom.xml` dependencies, not Maven-profile-gated. Which provider
actually resolves models is a runtime decision (the key you set), not a build-time one. So the app
boots the same way for every learner, keyed or not.

To keep the keyless build green, `application.yml` gives each provider's `api-key` a placeholder
fallback (`${OPENAI_API_KEY:noop-…}`); a real key from the environment or `.env` overrides it. A
placeholder never makes a live call succeed — for a no-key run use the `mock` profile.

## Maven profiles

In `pom.xml`:

| Profile | Activation | Effect |
|---|---|---|
| `observability` | manual (`-Pobservability`) | Adds `embabel-agent-starter-observability` + `opentelemetry-exporter-zipkin`. |
| `eval` | manual (`-Peval`) | Flips Surefire to run **only** `@Tag("eval")` tests (the eval lane); the default build **excludes** that tag via `<excludedGroups>eval</excludedGroups>`. Needs a provider key or the cases self-skip. See [Run the eval lane](../how-to/run-the-eval-lane.md). |

Pinned versions: `embabel-agent.version` = `0.5.0`, `java.version` = `21`, Spring Boot parent =
`3.5.13`.

## Environment variables (.env)

`.env` is git-ignored; copy `.env.example` to `.env` and set one provider key. `.env` is loaded into
the Spring `Environment` at startup by `spring-dotenv`, so a key placed there is picked up without
exporting it. Keys are only needed to run the agent against a real model — the build and tests are
green without any key.

| Variable | Description |
|---|---|
| `OPENAI_API_KEY` | OpenAI provider key (default provider). Read via `${OPENAI_API_KEY}` in `application.yml`. |
| `ANTHROPIC_API_KEY` | Anthropic provider key. Read via `${ANTHROPIC_API_KEY}` in `application.yml`. |

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
