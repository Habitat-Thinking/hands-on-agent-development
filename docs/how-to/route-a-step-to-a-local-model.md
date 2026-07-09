# Route a step to a local model

You want one step's data to **stay in the building** — a regulated field the attendee typed, say —
so you route just that action to a local model instead of a cloud provider. This is the same
`withLlmByRole` lever as [routing by role](route-models-by-role.md), pointed at a model running on
your own machine. **No code changes and no cloud key**: the OpenAI provider is already on the
classpath with a placeholder key, and a local server ignores the key anyway.

This is also the one keyless way to watch routing bind to a *real* model. The [`mock`
profile](use-mock-mode.md) proves the *routing* (which role each action carries) but spends no tokens
and calls no model; a local model gives you genuine output with nothing leaving your machine and
nothing spent.

## Before you start

Install a local server that speaks the OpenAI API and pull a model. [Ollama](https://ollama.com)
is the simplest — it serves an OpenAI-compatible API on port `11434`:

```bash
ollama pull llama3.2        # or any tag you have; llama3.2 is small and fast
ollama serve                # if it is not already running (check: curl localhost:11434)
```

## 1. Add a `local` Spring profile to `application.yml`

Append a new profile section — the same `---` / `on-profile` pattern the `mock` and `observability`
profiles already use. It does two things: point the already-present OpenAI provider at Ollama's
endpoint, and bind the role(s) you want local to a local model tag.

```yaml
---
# Local-model profile: keep a step's data in the building by routing its role to Ollama.
# Activate with: SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
spring:
  config:
    activate:
      on-profile: local
embabel:
  agent:
    platform:
      models:
        openai:
          # Send the OpenAI provider's calls to the local server instead of api.openai.com.
          base-url: http://localhost:11434/v1
  models:
    # A local server has none of the gpt-* models, so bind every role that resolves through
    # the OpenAI provider to a local tag. (base-url is provider-wide — see the caveat below.)
    default-llm: llama3.2
    llms:
      cheapest: llama3.2
      best: llama3.2
```

No key is required: Ollama ignores the API key, and `application.yml` already supplies a placeholder
fallback for `openai.api-key`, so the app boots and runs with nothing in `.env`.

## 2. Run against the local model

```bash
SPRING_PROFILES_ACTIVE=local ./mvnw spring-boot:run
```

Because this is a *real* model (not the deterministic stub), the normal `x` command works — use it
and read the plan:

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
```

The planning log shows each action resolving to your local tag (`llama3.2`) rather than a `gpt-*`
model. Nothing was sent off your machine and no cloud key was used. (Unlike `mock` mode you get real,
varying output; unlike the cloud path you spend nothing — this is why `x`, not `plan`, is fine here:
a local model can answer the goal-ranking call that the mock cannot.)

## 3. Keep the build green and record the trade

The `local` profile is inactive by default — exactly like `mock` and `observability` — so the mocked
tests are untouched:

```bash
./mvnw -q verify        # still green, no key, no Ollama needed
```

A local model buys data residency at the cost of latency and usually some quality. Record that trade
in `MODEL_ROUTING.md`'s token-budget table rather than making it silently — the same instinct as
everywhere else in the workshop: a decision you can see and defend beats a default you never examined.

## Keeping some steps in the cloud (true hybrid) — the honest caveat

`base-url` is **provider-wide**: once you point the OpenAI provider at Ollama, *every* role that
resolves through it goes local. That is why the recipe above routes all roles to a local tag. The
genuine regulated pattern — one sensitive step local, the rest in the cloud — needs the two steps on
*different providers*: keep the local step on the OpenAI provider (pointed at Ollama) and bind the
cloud roles to the **Anthropic** provider, which the OpenAI `base-url` override does not touch (set
`ANTHROPIC_API_KEY` and point `best`/`default-llm` at `claude-*` models — see
[route models by role](route-models-by-role.md)). That is a real key for the cloud half; only the
all-local recipe above is fully keyless.

!!! note "If a local tag will not resolve"
    Embabel maps a model *name* to a provider. If `llama3.2` is not picked up, confirm Ollama is
    serving (`curl localhost:11434/v1/models`) and that the tag matches one you have pulled; Embabel
    0.5.0 also ships first-class Ollama model definitions (`com.embabel.agent.api.models.OllamaModels`)
    if you prefer to register the model natively rather than via the OpenAI-compatible endpoint. The
    routing *mechanism* (a role bound to a local model under a profile) is the same either way.

---

For the general role→model mechanism, see [route models by role](route-models-by-role.md); for the
config keys, the [model-routing reference](../reference/model-routing.md) and the
[configuration reference](../reference/configuration.md); and for *why* data residency meets the same
knob as cost, [About model routing](../explanation/right-sizing-models.md). This is the hands-on
version of the Lab 6 "going further" regulated-environment lever.
