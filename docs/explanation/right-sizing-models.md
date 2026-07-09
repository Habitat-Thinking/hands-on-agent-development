# About right-sizing models

There is a habit of reaching for the strongest available model for everything, on the theory that
more capability never hurts. It does hurt — in cost, in latency, and, in regulated settings, in where
your data is allowed to go. ConfPlanner treats the choice of model as a *design parameter*, decided
per action and recorded as a deliberate trade. This page is about why that choice deserves thought,
and how to make it well.

## Cost is a design parameter, not an afterthought

Every action in ConfPlanner spends tokens, and the spend is wildly uneven across the pipeline.
Indicative shares of a single schedule run:

| Stage | Share of tokens | Why |
|---|---|---|
| extract + shortlist | ~15% | Many tokens *in* (the catalog menu), few out |
| research | ~35% | One reasoned insight per shortlisted session |
| assemble | ~45% | The bulk: reasoning plus a written rationale |
| confirm | ~0% | Plain code — no model at all |

If every action used your best model, you would be paying premium rates to pull five fields out of a
sentence. The opposite of right-sizing is not "be cheap"; it is *not deciding* — letting one default
model absorb work it is over-qualified for. Right-sizing makes the cost of each step a choice you
made on purpose, which is the only kind of cost you can later defend or reduce.

Two corollaries fall out of treating cost as a parameter. First, ConfPlanner caps each run with a
`Budget(0.50, 20, 200_000)` so a misbehaving plan fails fast rather than spinning — the budget is the
floor under the routing decision (see [guardrails](guardrails-and-invariants.md)). Second, the rule
is *measure before optimising*: run the same request all-`best` versus routed and compare the budget
line in the planning log. A routing decision you cannot see in the log is a guess.

## Route by return-type complexity

The question "which model?" has a surprisingly mechanical answer in ConfPlanner, and it keys off the
*shape of what the action returns*:

> If the return type is a flat list of strings, route `cheapest`. If it carries judgement — scores, a
> rationale, a conflict-free arrangement — route `best`.

Watch it applied across the pipeline:

| Action | Role | Why |
|---|---|---|
| `extractAttendeeProfile` | `cheapest` | Pull a few fields from one sentence |
| `shortlistSessions` | `cheapest` | Match tags to a menu of options |
| `researchSessions` | `best` | Reason about relevance and *score* each pick |
| `assembleSchedule` | `best` | Synthesise a *conflict-free* schedule plus a *rationale* |
| `confirmSchedule` | _(none)_ | Plain code — repackages a validated draft |

The heuristic works because the return type is an honest proxy for how much judgement the step
demands. Extracting a profile or shortlisting by tag is pattern-matching that produces lists; a
cheap model does it reliably. Scoring relevance or arranging a clash-free day is reasoning that
produces structured judgement; that is what the strong model is for. The id-only idiom helps here
too — because LLM actions emit just ids and reasoning while plain code resolves the rest (see
[DICE](dice.md)), each model's job is as small as it can be, which widens the range of steps a cheap
model can safely own.

## Routing is configuration, not code

The mechanism keeps the decision *out* of the Java. Code calls `withLlmByRole("cheapest")` or
`withLlmByRole("best")`, and `application.yml` maps those roles to concrete models:

```yaml
embabel:
  models:
    default-llm: gpt-4.1-mini
    llms:
      cheapest: gpt-4.1-nano
      best: gpt-4.1
```

This separation is deliberate and has a hard-won reason behind it: model names change between
releases, so ConfPlanner never hard-codes one in Java. An action commits to a *role* — a statement
about how much capability the work needs — and the environment binds that role to whatever model is
current or available. Re-routing the whole agent to different models is then an edit to config, not a
rewrite of code; and because the tests mock the LLM, you can re-route and still build green with no
keys. The build-time twin is `MODEL_ROUTING.md`, where the harness asks you to *justify* each
action's role by return-type complexity — the routing decision is reviewed, not just made.

## The local-model and data-residency angle

The same `withLlmByRole` lever answers a question that has nothing to do with cost: *what if this
step's data is not allowed to leave the building?*

Because a role is just a name bound in config, you can point `cheapest` (or any role) at a **local**
model — an Ollama tag, say, under a Spring profile — for a step whose data must stay on-premises. The
extraction step reads the attendee's raw words; in a regulated setting you might route exactly that
step to a local model and keep the rest in the cloud. Nothing in the action's code changes; only the
role-to-model binding does.

This is the trade worth naming explicitly, because it is a genuine trade and not a free win: a local
model typically costs you latency and some quality in exchange for data residency. ConfPlanner's
guidance is to record that trade in `MODEL_ROUTING.md`'s token-budget table rather than make it
silently — the same instinct as everywhere else in the workshop, that a decision you can *see and
defend* beats a default you never examined. Routing is where cost, capability, and data sovereignty
all meet a single configurable knob.

For the hands-on recipe — a `local` Spring profile, the OpenAI-compatible endpoint, and a key-free
run against Ollama — see [Route a step to a local model](../how-to/route-a-step-to-a-local-model.md).

To *do* the routing yourself, work Lab 6 in the [tutorials](../tutorials/index.md). For the exact
`withLlmByRole` idiom, the role names, and the budget constructor as lookup, see the
[reference](../reference/index.md); for the routing table you maintain, `MODEL_ROUTING.md`.
