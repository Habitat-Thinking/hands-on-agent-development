# Run a deterministic demo with mock mode

You want a repeatable run in the room that does not depend on a live model, spends no tokens, and
needs no API key — for a demo, an offline machine, or a flaky-network venue.

## Run with the `mock` profile

```bash
SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run
```

This activates the `mock` Spring profile, which registers a deterministic in-JVM stub model and
routes every LLM role (`default-llm`, `cheapest`, `best`) to it. The stub matches each ConfPlanner
prompt and returns canned, valid responses — so a run completes end-to-end (you get a real
`PersonalSchedule`) while **no provider key is read and no network call is made**. It spends no
tokens and works fully offline.

> The output is fixed (the same platform-leaning schedule every time) — that is the point: a
> repeatable demo. For a run against your own request and a real model, use a key instead
> (see [Run against a real model](run-with-a-real-model.md)).

> Pass the profile through the environment, not `-Dspring-boot.run.profiles=mock`: the Boot plugin
> forwards that flag as a program argument, which Spring Shell tries to execute as a command and
> fails with `CommandNotFound`.

## Invoke the goal with `plan`

Use the `plan` command — it invokes the schedule goal **directly**:

```text
plan "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"
```

`plan` prints the planning log (the derived action sequence and the world-state, cycle by cycle) by
default, so you can read the plan with no extra flags.

!!! warning "In mock mode use `plan`, not `x`"
    `x` / `execute` first ask the model to **rank** your free-text request against the registered
    goals — and that ranking is itself an LLM call, which the deterministic mock does not answer. So
    `x "..."` fails under the `mock` profile with `Text content cannot be empty` (the goal ranker
    gets an empty response). The `plan` command skips ranking by invoking `PersonalSchedule`
    directly, which is why it is the key-free path. The `-p` / `-r` verbosity flags exist only on
    `x` (the real-model path); `plan` takes no flags but already prints the plan.

## When to use it

- A live demo that must not depend on the network or on token budget.
- A quick smoke-run with no `.env` configured.

For a run against a real model, set a key and follow
[Run ConfPlanner against a real model](run-with-a-real-model.md) instead.

---

The `mock` profile is defined in `application.yml`; see the
[configuration reference](../reference/configuration.md) for the full profile list. For *why* mock
mode exists alongside the test harness, see
[About the runtime harness](../explanation/the-runtime-harness.md).
