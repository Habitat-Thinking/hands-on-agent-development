# Setup

## 1. Java 21+

```bash
java -version    # need 21 or newer
```
The repo ships the Maven wrapper (`./mvnw`), so you do **not** need a system Maven.

## 2. Build (no keys needed)

```bash
./mvnw clean verify
```
This is green out of the box: the unit and integration tests **mock the LLM** (via
`embabel-agent-test`), so no API keys and no network model calls are required to build or to run the
test suite. Keys are only needed to run the agent against a *real* model (step 3).

> The `lab4-broken` branch is the deliberate exception — it compiles but the agent goes `STUCK` at
> runtime, so `./mvnw verify` fails there on purpose.

## 3. Keys (only to run the agent for real)

```bash
cp .env.example .env
# edit .env and set ONE provider key:
#   OPENAI_API_KEY=...        (default; application.yml routes to OpenAI models)
#   ANTHROPIC_API_KEY=...
```
`.env` is git-ignored. **Never commit a real key.** The Maven profiles auto-activate the matching
provider starter when the env var is present.

Run the shell:
```bash
./mvnw spring-boot:run
# then, in the shell:
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"
x "..." -p -r     # -p prompts, -r responses — read the plan, not the vibes
```

## 4. Mock mode (deterministic demos, no model)

For a demo that must not depend on a live model, run with the `mock` profile:
```bash
SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run
```
This sets `embabel.agent.platform.test.mock-mode=true`. Use it when you want a repeatable run in the
room without spending tokens.

> Pass the profile through the environment, not `-Dspring-boot.run.profiles=…`: the Boot plugin
> forwards that flag as a *program argument*, which Spring Shell tries to run as a command and fails
> with `CommandNotFound`.

## 5. Observability (Lab 4, optional — needs Docker)

```bash
docker compose up -d        # Zipkin on http://127.0.0.1:9411
SPRING_PROFILES_ACTIVE=observability ./mvnw -Pobservability spring-boot:run
# open http://127.0.0.1:9411 and find your run
docker compose down
```
The `observability` Maven profile adds the OpenTelemetry + Zipkin exporter; the `observability`
Spring profile points tracing at the local collector. **Without Docker**, use the planning-log path
instead (`x "..." -p -r`) — it shows the same world-state the trace does. There is nothing you
*must* run in Docker for the default labs.

## 6. MCP tools (optional)

The Lab 3 `@SecureAgentTool` is enforced when tools are exposed over an MCP server with an
authenticated caller. The default shell excludes that web-security auto-config so it boots without a
web server; enabling MCP is an advanced exercise (see the Embabel `Secured` example and
`application-secured.yml`). The labs do not require it.

## 7. Embabel help inside your coding agent (optional, Track C)

Embabel's **Guide** project (<https://github.com/embabel/guide>) is itself an MCP server that
answers Embabel questions with RAG over the docs — and you can wire it into the same Claude Code
you use as the build-time harness. Track C then has framework-accurate help on tap while the
orchestrator works: the dual harness eating its own dog food.

```bash
# from the guide repo's README — register it as an MCP server in Claude Code, e.g.:
claude mcp add embabel-guide -- <run command from the guide README>
```

Also worth bookmarking: **Embabel Hub** (<https://hub.embabel.com>) — a talk-to-the-docs agent —
and the `embabel-agent-examples` repo (tracks snapshots, i.e. current best practice).

## 8. GitHub Codespace / devcontainer (kill setup friction in the room)

To avoid per-laptop setup, consider running the workshop in a **GitHub Codespace** (or a local
devcontainer): JDK 21 preinstalled, `./mvnw clean verify` on first boot, and keys provided as
Codespace secrets. This gets a room of attendees to a green build in minutes rather than fighting
JDK versions.
