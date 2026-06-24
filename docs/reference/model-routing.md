# Model routing

How ConfPlanner maps **LLM roles** to concrete models, and the keys that control the mapping. Code
calls `withLlmByRole("cheapest"|"best")` (or `withDefaultLlm()`); `application.yml` binds each role
to a model, so the same action can run on different models in different deployments without code
changes. This page is the authoritative routing table.

## Roles and the actions that use them

| Role | Call | Default model | Used by |
|---|---|---|---|
| `cheapest` | `withLlmByRole("cheapest")` | `gpt-4.1-nano` | `extractAttendeeProfile`, `shortlistSessions` |
| `best` | `withLlmByRole("best")` | `gpt-4.1` | `researchSessions`, `assembleSchedule` |
| `default` | `withDefaultLlm()` | `gpt-4.1-mini` | `premiumBriefing`, `planNetworking` |

`loadCatalog` and `confirmSchedule` use no model (plain code). No action declares a web tool group.

## Configuration keys

Set in `src/main/resources/application.yml` under `embabel.models`:

| Key | Description |
|---|---|
| `embabel.models.default-llm` | Model for `withDefaultLlm()` and the `default` role |
| `embabel.models.llms.cheapest` | Model bound to the `cheapest` role |
| `embabel.models.llms.best` | Model bound to the `best` role |
| `embabel.models.llms.<name>` | Bind any custom role name, then call `withLlmByRole("<name>")` |

Swap any of these for a model your configured provider exposes — including a local Ollama tag for a
role whose data must not leave the building.

## Where it connects

- Set a provider/credentials: [how to run with a real model](../how-to/run-with-a-real-model.md).
- Assign models per role, hands-on: [how to route models by role](../how-to/route-models-by-role.md).
- *Why* route by return-type complexity, and the data-residency trade-off:
  [right-sizing models](../explanation/right-sizing-models.md).
- The per-action rationale table also lives in the repo's `MODEL_ROUTING.md`.
