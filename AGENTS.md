# AGENTS.md

Project knowledge for agents (and humans) working on ConfPlanner. Tuned for Embabel 0.5.0 + Java 21.

## STYLE

- Java records for all domain types; immutable; no untyped `Map`/`Object` passing between actions.
- Literate code: each source file opens with a short narrative preamble explaining *why* it exists.
- Match the surrounding comment density and naming. Prompts are Java text blocks kept next to the
  action that uses them.
- One logical change per commit; conventional commit messages.

## GOTCHAS

Hard-won Embabel 0.5.0 facts (all verified against source/runtime in this repo):

- **Postconditions don't gate the goal.** `@Action(post={"x"})` on the `@AchievesGoal` action is a
  planning promise; the goal is "achieved" once the output type is produced. To enforce an
  invariant, make a *separate* goal action `pre`-require it, and give the producer `canRerun=true`.
  (See `assembleSchedule` → `confirmSchedule`.)
- **One goal per agent.** Two `@AchievesGoal` actions in one `@Agent` → *neither* goal registers
  ("has 2 @AchievesGoal actions, while one is supported"). Use a second `@Agent`.
- **Agents plan only with their own actions.** A separate agent cannot borrow another agent's — or
  an `@EmbabelComponent`'s — actions for planning (you get `STUCK`, history 0). Share the *logic*
  via a plain `@Service` and declare thin `@Action` wrappers in each agent.
- **Preconditions need a producer.** A `pre={"hasCandidates"}` is unreachable unless some action
  declares `post={"hasCandidates"}`. Name conditions by their `@Condition(name=...)`.
- **`@Action` has no `toolGroups`.** Add tool groups on the prompt runner:
  `ai.withDefaultLlm().withToolGroup(CoreToolGroups.WEB)`. `CoreToolGroups.WEB` is the string `"web"`.
- **Typed output idiom:** `ai.withLlmByRole("best").creating(T.class).fromPrompt(prompt)` (the older
  `createObject(prompt, T.class)` still exists). `withTemplate` is deprecated → `rendering(...)`.
- **`@SecureAgentTool` (package `com.embabel.agent.mcpserver.security`)** needs Spring MVC; on a
  shell app exclude `com.embabel.agent.autoconfigure.mcpserver.security.AgentMcpServerAutoConfiguration`.
- **Models change between releases.** Don't hard-code model names in Java; use roles
  (`embabel.models.llms.*`) and `withLlmByRole`.

## ARCH_DECISIONS

- LLM actions emit only session **ids** (+ reasoning); plain code resolves ids → `Session` from the
  catalog. Keeps the model's job small/reliable and the catalog the single source of truth.
- The shared pipeline (extract/load/shortlist/research) lives in `ConfPlanningCapabilities`
  (`@Service`); each agent declares thin `@Action` wrappers over it.
- Guardrails are framework-enforced (`@Condition` invariant the goal depends on, precondition,
  `Budget`), not prompt instructions.

## TEST_STRATEGY

- Unit: `FakeOperationContext` — queue a response, call one action, assert on the captured prompt or
  the returned domain object. No Spring, no keys.
- Integration: `EmbabelMockitoIntegrationTest` — boot the real platform + catalog, mock each
  `createObject` by prompt substring, `AgentInvocation` the goal, assert plan completion.
- Every guardrail has a test that proves it bites (e.g. `GuardrailEnforcementTest`: a clashing draft
  never reaches the goal). `lab4-broken` is the documented exception that fails at runtime.

## DESIGN_DECISIONS

- Catalog is **synthetic and fictional** (~35 sessions, 4 tracks, 3 days) with deliberate slot
  overlaps so the double-booking invariant has something to catch. Never scrape the real schedule.
- Logging personality is off by default (`embabel.agent.logging.personality: default`).
- Secured/observability/MCP capabilities are opt-in (profiles) so the default no-Docker path stays
  green and lean.
