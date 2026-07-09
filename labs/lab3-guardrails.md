# Lab 3 — Guardrails: preconditions & invariants

> **Goal:** make it impossible for the agent to hand back a double-booked schedule, an empty one,
> or to run forever — and refuse a premium tool to callers who lack authority.
> **Objective:** make the contract explicit; the framework enforces it, not a code review.
> **Habit:** **Make the contract explicit.**
> **Ladder rung:** Track C (Regulating → Orchestrating).
> **Branches:** `lab3-before` → `lab3-after`.

## Why this lab

Four distinct guardrail shapes, each with a home in Embabel:

| Guardrail | Mechanism | Reassessed… |
|---|---|---|
| Invariant (no double-booking) | `@Condition` as a **postcondition** + a goal that **requires** it | every planning cycle |
| Precondition (need candidates) | `@Condition` as a **precondition** | before the action runs |
| Budget (don't run forever) | `Budget` / `EarlyTerminationPolicy` via `ProcessOptions` | continuously |
| Access control (premium tool) | `@SecureAgentTool` SpEL authority | before the tool runs |

**The subtle bit — a postcondition alone does not enforce.** In Embabel a `post` on the goal
action is a *planning promise*, not a runtime gate: the goal is "achieved" as soon as the action
produces its output type, whether or not the post still holds. To make `noDoubleBooking` actually
bite you must make the **goal depend on it**. The clean pattern (mirrors the framework's own
Researcher example) is to split assembly in two:

- `assembleSchedule` produces a **`DraftSchedule`** with `post = {"noDoubleBooking"}, canRerun = true`;
- `confirmSchedule(DraftSchedule)` is the `@AchievesGoal` action with `pre = {"noDoubleBooking"}`.

A clashing draft fails `noDoubleBooking`, so `confirmSchedule` cannot run; the planner re-runs
assembly, and if no clash-free option exists it stops at the **budget** rather than returning a
broken schedule.

## Before state (`lab3-before`)

- `assembleSchedule` is the goal and produces `PersonalSchedule` directly. Nothing stops a
  double-booked or empty schedule; no budget; no secured action.
- `// TODO (Lab 3)` anchor above `assembleSchedule`.

## Steps

1. **Add `DraftSchedule`** (`List<ScheduleItem> items, String rationale`).
2. **Invariant condition** (side-effect-free):
   ```java
   @Condition(name = "noDoubleBooking")
   boolean noDoubleBooking(DraftSchedule draft) {
       var slots = draft.items().stream().map(ScheduleItem::slot).toList();
       return slots.size() == new java.util.HashSet<>(slots).size();
   }
   ```
3. **Precondition condition:** `@Condition boolean hasCandidates(CandidateSessions c)` →
   `c != null && !c.sessions().isEmpty()`.
4. **Split the goal:** make `assembleSchedule` return `DraftSchedule` with
   `@Action(pre = {"hasCandidates"}, post = {"noDoubleBooking"}, canRerun = true)`, and add
   `@AchievesGoal @Action(pre = {"noDoubleBooking"}) PersonalSchedule confirmSchedule(DraftSchedule)`.
   Have `shortlistSessions` post `hasCandidates` (`@Action(post = {"hasCandidates"})`).
5. **Budget** in `ConfPlannerShell` — `Budget(cost, actions, tokens)`: max USD spend, max action
   executions, and max tokens per run.
   ```java
   var options = ProcessOptions.DEFAULT.withBudget(new Budget(0.50, 20, 200_000));
   AgentInvocation.builder(agentPlatform).options(options).build(PersonalSchedule.class).invoke(...);
   ```
   > What actually stops the assemble → confirm re-run loop is the platform's **default**
   > `MaxActionsEarlyTerminationPolicy(maxActions = 50)` — that is the policy name you'll see in the
   > log — with this `Budget` acting as the cost/token backstop. Drop the `Budget`'s action count
   > below 50 and *it* becomes the stop; leave it high and the default 50-action policy halts the loop.
   > Either way the run ends with `error=true` and `invoke` throws rather than returning a clash. (This
   > `Budget` is set on the shell `plan` command, so it governs that command only; the key-free proof
   > of the stop is `GuardrailEnforcementTest`, not `x "..."`.)
6. **Access control:** first create the side type — a `PremiumBriefing` record (e.g. one
   `String summary` field) in `domain/`, the same way you added `DraftSchedule` in Step 1. Then add
   `@SecureAgentTool("hasAuthority('conf:premium')")` (package `com.embabel.agent.mcpserver.security`)
   on a premium action producing that `PremiumBriefing` — the goal never consumes it, so the free
   flow is untouched.
7. Build: `./mvnw -q verify`.

> **Your diff will also show…** the `lab3-after` reference adds test content the steps above don't
> spell out: two `@Condition` unit tests and the whole `GuardrailEnforcementTest` (the key-free
> proof that a double-booked draft never reaches the goal). `GuardrailEnforcementTest` does not exist
> on `lab3-before` — you write it in the `EmbabelMockitoIntegrationTest` style, mocking each step.
> **To force a guaranteed clash,** mock the draft to place two sessions that share a slot: the slot
> key is `Session.slot()` = `day + " " + startTime` (there is no literal `slot` field), and on the
> synthetic catalog `PC-01` and `AI-01` both sit at `2026-09-15 09:00`. The existing
> `ConfPlannerAgentIntegrationTest` is the closest template — copy its mock wiring and the inner
> record names (`Shortlisting`, `ResearchOutput`, `Insight`, `ScheduleDraft`).
>
> **You do not touch `pom.xml` or `application.yml` in this lab.** The mcpserver-security dependency
> and the `AgentMcpServerAutoConfiguration` exclusion Step 6 relies on are **already provided on
> `lab3-before`** (so `@SecureAgentTool` compiles and the shell-only app still boots) — which is why
> `git diff lab3-before lab3-after -- pom.xml src/main/resources` is empty. Read them if you're
> curious how the secured tool is wired; there is no pom/yml edit to make.

## Acceptance check (framework-enforced)

- A draft with two sessions in one slot **never** reaches the goal (`GuardrailEnforcementTest`
  asserts `invoke` throws — the planner re-runs and hits the budget instead of returning a clash).
- An empty shortlist fails the `hasCandidates` precondition cleanly.
- The budget stop is visible in the planning log (`MaxActionsEarlyTerminationPolicy`).
- The secured action denies callers lacking `conf:premium` with no LLM spend (over MCP).

## Three-track notes

- **Track A:** add each condition by hand; run `GuardrailEnforcementTest` and watch the clash get
  refused.
- **Track B:** ask an ungoverned agent to "stop double-booking." Does it hide the check in the
  prompt (hope) or add a reassessed invariant the goal depends on (guarantee)?
- **Track C:** the harness constraint "every action that mutates state or calls a tool is guarded;
  conditions are side-effect-free" is exactly this lab.

## Going further

- Make `noDoubleBooking` forbid *overlapping* times, not just identical start slots.
- Run the secured example with JWT (`application-secured.yml` in the Embabel examples) and watch a
  `conf:premium` vs non-premium token get different results.
- **The fifth shape — a content guardrail.** The four shapes above guard the *plan*; Embabel's
  guardrail framework (0.3.3+) also guards the *content* of every LLM exchange. `main` carries a
  worked example: `RequestContentGuardRail` implements `UserInputGuardRail` and screens the raw
  attendee request for prompt-injection markers before any model sees it — deterministic, no LLM
  spend on a rejected request. It is attached with `.withGuardRails(...)` on the prompt runner in
  `ConfPlanningCapabilities.extractProfile`, and `RequestContentGuardRailTest` proves it bites
  with no keys. Be honest about what it is: a **cheap deterministic pre-filter** over a handful of
  enumerated phrases, **not a security boundary** — a competent attacker rephrases around a fixed
  deny-list in seconds. Its value is dropping low-effort garbage before you spend on a model call;
  the real guarantee against a *successful* injection is structural — a poisoned `assembleSchedule`
  still can't satisfy `noDoubleBooking`, so the schedule invariant holds whether or not this filter
  caught anything. Try adding a marker of your own and watch the test catch it — then compare
  this to Lab 1's belt-and-braces: DICE protects the schedule from a model that slips; the content
  guard cheaply screens input that lies, and the structural invariant catches what it misses. (See
  the how-to guide *Add a guardrail* for the recipe.)
