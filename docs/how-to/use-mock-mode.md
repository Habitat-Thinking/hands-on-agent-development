# Run a deterministic demo with mock mode

You want a repeatable run in the room that does not depend on a live model, spends no tokens, and
needs no API key — for a demo, an offline machine, or a flaky-network venue.

## Run with the `mock` profile

```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=mock
```

This activates the `mock` Spring profile, which sets
`embabel.agent.platform.test.mock-mode=true`. No provider key is read and no model is called.

## Invoke a goal as usual

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"
x "..." -p -r
```

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
