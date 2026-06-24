# Enable Zipkin tracing

You want a visual trace of a run — which action executed, how often, and whether the goal span
closed. This needs Docker. If you do not have Docker, skip to
[Without Docker](#without-docker-read-the-planning-log).

## 1. Start the Zipkin collector

```bash
docker compose up -d        # Zipkin on http://127.0.0.1:9411
```

## 2. Run with the observability profiles

You need the Maven `observability` profile (to put the OpenTelemetry + Zipkin exporter on the
classpath) **and** the `observability` Spring profile (to point tracing at the local collector):

```bash
./mvnw -Pobservability spring-boot:run -Dspring-boot.run.profiles=observability
```

## 3. Invoke a goal

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"
```

## 4. Open the trace

Open <http://127.0.0.1:9411>, find your run, and inspect the spans — each action is a span. A stuck
plan shows up as one action (e.g. `assembleSchedule`) executing repeatedly while the goal span never
closes.

## 5. Tear down

```bash
docker compose down
```

## Without Docker: read the planning log

You do not need Docker for any default lab. The planning log shows the same world-state the trace
does. Run with prompt and result verbosity:

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
```

Read the world-state lines for the condition that never flips to `TRUE` — that is the same evidence
the trace gives you.

---

For the trace-vs-log distinction and how to debug a stuck plan, see Lab 4 in the
[tutorials](../tutorials/index.md) and
[About explainability](../explanation/explainability.md). For the exact profile keys, see the
[configuration reference](../reference/configuration.md).
