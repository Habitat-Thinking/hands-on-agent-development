# CLAUDE.md

Build-time harness for this repo. Read this and `AGENTS.md` before making changes.

## Workflow

- This is a teaching repo: **history is the lesson**. Use conventional commits, one logical change
  per commit, matching the lab progression. Do not squash labs together.
- Develop on a branch; create lab branches at the right commit. The branch contract is fixed —
  `main`, and `lab{1..6}-{before,after}` plus `lab4-broken` (see README walk order).
- Every change ends with the ritual: read the planning log, read the trace, confirm the acceptance
  check stated in the lab worksheet.
- Track C is the preferred way to make changes: orchestrator plans → `java-implementer` edits under
  `src/main/java/**` → constraint-gate / two-stage review → land.

## Build and Test

```bash
./mvnw -q verify        # compile + unit + Mockito integration tests; green with NO API keys
./mvnw spring-boot:run  # start the Embabel shell; then: x "..." -p -r
```

- Tests mock the LLM (`embabel-agent-test`: `FakeOperationContext`, `EmbabelMockitoIntegrationTest`,
  `whenCreateObject`/`whenGenerateText`). Never require a real key for the build to pass.
- The **only** branch allowed to fail `verify` is `lab4-broken` (compiles, stalls at runtime).
- Pinned: Embabel `0.5.0`, Java `21`, Spring Boot `3.5.x`.

## Learnings

A running log of what bit us, so it doesn't bite again. (Detail lives in `AGENTS.md` → GOTCHAS.)

- A `post` condition on an `@AchievesGoal` action is a planning promise, **not** a runtime gate. To
  make an invariant bite, the goal must **require** it as a `pre` (split assemble → confirm).
- An Embabel `@Agent` supports **one** goal type and plans **only with its own actions**. Share
  logic across agents via a plain `@Service`, not via `@EmbabelComponent` (its actions are not
  pooled into another agent's planning) and not via a second `@AchievesGoal` in the same class.
- The current typed-output idiom is `ai.with…Llm().creating(T.class).fromPrompt(...)`. `@Action` has
  no `toolGroups` member — add tool groups on the prompt runner (`withToolGroup(CoreToolGroups.WEB)`).
- `@SecureAgentTool` needs Spring MVC; on a shell-only app, exclude
  `AgentMcpServerAutoConfiguration` so the app still boots.
