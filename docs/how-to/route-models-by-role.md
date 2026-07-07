# Route models by role

You want each action to use a model sized for its job — cheap for extraction, strong for synthesis —
without hard-coding model names in Java. Routing is a configuration decision: code calls
`withLlmByRole("...")` and `application.yml` maps roles to models.

## 1. Route the action in code

Choose a role for the action — `cheapest` for simple extraction, `best` for synthesis (the
[right-sizing models](../explanation/right-sizing-models.md) explanation gives the rule of thumb) —
and set it on the prompt runner:

```java
// cheap work — pulling fields from a sentence
ai.withLlmByRole("cheapest").creating(AttendeeProfile.class).fromPrompt(...);

// the hard step — a reasoned, conflict-free schedule
ai.withLlmByRole("best").creating(ScheduleDraft.class).fromPrompt(...);
```

Do not hard-code a model name in Java — models change between releases. Change only the role string;
the logic stays untouched.

## 2. Map the roles in `application.yml`

=== "OpenAI (default)"

    ```yaml
    embabel:
      models:
        default-llm: gpt-4.1-mini
        llms:
          cheapest: gpt-4.1-nano
          best: gpt-4.1
    ```

=== "Anthropic"

    ```yaml
    embabel:
      models:
        default-llm: claude-sonnet-5     # must resolve, or the app has no default LLM
        llms:
          cheapest: claude-haiku-4-5
          best: claude-opus-4-8
    ```

    Point the roles at Anthropic models and set `ANTHROPIC_API_KEY` (see
    [Run against a real model](run-with-a-real-model.md)). **`default-llm` must change too** — every
    action that calls `withDefaultLlm()` (e.g. `planNetworking`) resolves it, so leaving it on a
    `gpt-4.1*` id while routing the roles to `claude-*` leaves the app with no resolvable default.

Swap these for whatever you actually have — including a local Ollama tag for `cheapest` when data
must stay in the building.

## 3. Update `MODEL_ROUTING.md`

Keep the routing table in `MODEL_ROUTING.md` matching the code: one row per action with its role,
the justification (return-type complexity), and the default model. The repo's table already covers
`extractAttendeeProfile`/`shortlistSessions` (`cheapest`), `researchSessions`/`assembleSchedule`
(`best`), `planNetworking`/`premiumBriefing` (`default`, via `withDefaultLlm()`), and the plain-code
steps (`loadCatalog`, `confirmSchedule`). When you add or re-route an action, add or edit its row
there. (`planNetworking` uses the **default** LLM — it is not routed to `best`.)

## 4. Build

```bash
./mvnw -q verify
```

Tests mock the LLM, so routing changes stay green without any key.

## If you want to verify which model each action used

Run the shell and read the plan:

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
```

## If a step's data cannot leave the building

Point its role at a local model (e.g. an Ollama tag) under a Spring profile, and note the
latency/quality trade-off in the token-budget table in `MODEL_ROUTING.md`.

---

For the full role table and default models, see the
[model-routing reference](../reference/model-routing.md). For *why* cost is a design parameter and
how to think about the cheapest model that passes, see
[About model routing](../explanation/right-sizing-models.md).
