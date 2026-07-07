# Run ConfPlanner against a real model

You want the agent to call a live LLM and build a real schedule. The build and tests are green
without any key (they mock the LLM); a key is needed only to *run* against a real model.

## 1. Set a provider key

```bash
cp .env.example .env
```

Edit `.env` and set **one** key. `.env` is git-ignored — never commit a real key.

```bash
OPENAI_API_KEY=sk-...        # default; application.yml routes to OpenAI models
# or:
ANTHROPIC_API_KEY=sk-ant-...
```

Both provider starters are always on the classpath, and `.env` is loaded into the app at startup
(via `spring-dotenv`), so the key you set here is picked up automatically — there is nothing else to
configure. If you set the Anthropic key, also point the model roles at Anthropic models in
`application.yml` — see [Route models by role](route-models-by-role.md).

## 2. Start the shell

```bash
./mvnw spring-boot:run
```

## 3. Invoke a goal

In the shell, type a free-text request after `x` (alias for `execute`); the platform matches it to a
goal:

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"
```

To read the plan rather than just the answer, add prompt (`-p`) and result (`-r`) verbosity:

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
```

This is the same world-state a trace shows — see
[Enable Zipkin tracing](enable-zipkin-tracing.md) for the visual version.

## 4. (Optional) Invoke a specific goal directly

To call one goal by result type instead of letting the platform match, use the bundled `plan`
command, which invokes `PersonalSchedule` via `AgentInvocation`:

```text
plan "I'm a senior platform engineer into Kubernetes, resilience and DevEx"
```

## If you have no key

Run the deterministic mock instead — see
[Run a deterministic demo with mock mode](use-mock-mode.md).

---

For the full list of shell commands and flags, see the [CLI reference](../reference/cli.md). For role
names and their default models, see the [model-routing reference](../reference/model-routing.md).
