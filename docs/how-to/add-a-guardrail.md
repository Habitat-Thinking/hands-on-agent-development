# Add a guardrail

You want the framework — not a prompt instruction or a code review — to make a bad result
impossible: an invariant the goal must hold, a precondition before an action runs, and a budget so
the agent cannot run forever. This is the Lab 3 shape.

An invariant only bites if a **separate goal action `pre`-requires it** — use the `assemble → confirm`
pattern below. (Why a `post` on the goal action is not enough is covered in
[guardrails & invariants](../explanation/guardrails-and-invariants.md).)

## Add an invariant the goal pre-requires

### 1. Make the condition side-effect-free

```java
@Condition(name = "noDoubleBooking")
boolean noDoubleBooking(DraftSchedule draft) {
    var slots = draft.items().stream().map(ScheduleItem::slot).toList();
    return slots.size() == new java.util.HashSet<>(slots).size();
}
```

### 2. Split assembly into produce-then-confirm

The producer posts the invariant and can re-run; the goal action requires the invariant:

```java
@Action(pre = {"hasCandidates"}, post = {"noDoubleBooking"}, canRerun = true)
DraftSchedule assembleSchedule(AttendeeProfile profile, ResearchedSessions researched, Ai ai) { ... }

@AchievesGoal(description = "Produce a conflict-free personal schedule")
@Action(pre = {"noDoubleBooking"})
PersonalSchedule confirmSchedule(DraftSchedule draft) {
    return new PersonalSchedule(draft.items(), draft.rationale());
}
```

A clashing draft fails `noDoubleBooking`, so `confirmSchedule` cannot run; the planner re-runs
assembly. If no clash-free option exists, it stops at the budget rather than returning a broken
schedule.

## Add a precondition

A precondition needs a producer — a `pre={"hasCandidates"}` is unreachable unless some action
declares `post={"hasCandidates"}`.

```java
@Condition(name = "hasCandidates")
boolean hasCandidates(CandidateSessions candidates) {
    return candidates != null && !candidates.sessions().isEmpty();
}
```

Have the upstream action post it:

```java
@Action(post = {"hasCandidates"})
CandidateSessions shortlistSessions(AttendeeProfile profile, SessionCatalog catalog, Ai ai) { ... }
```

## Add a budget

Cap cost (USD), actions, and tokens so a stuck plan stops early. In `ConfPlannerShell`:

```java
var budget = new Budget(0.50, 20, 200_000);
var options = ProcessOptions.DEFAULT.withBudget(budget);

var schedule = AgentInvocation.builder(agentPlatform)
        .options(options)
        .build(PersonalSchedule.class)
        .invoke(new UserInput(request));
```

The budget stop is visible in the planning log (`MaxActionsEarlyTerminationPolicy`).

## (Optional) Gate a tool by authority

For a premium action off the goal path, add a Spring-Security authority expression. The annotation
is `com.embabel.agent.mcpserver.security.SecureAgentTool`:

```java
@SecureAgentTool("hasAuthority('conf:premium')")
@Action
PremiumBriefing premiumBriefing(ResearchedSessions researched, Ai ai) { ... }
```

This is enforced when the tool is exposed over an MCP server with an authenticated caller; a caller
lacking `conf:premium` is denied before any LLM spend. `@SecureAgentTool` needs Spring MVC, so the
shell app excludes `AgentMcpServerAutoConfiguration` to boot without a web server (already set in
`application.yml`).

## Add a content guardrail (screen input before any model sees it)

The shapes above guard the **plan**. A content guardrail guards the **content**: it validates the
raw user request (or a model's response) on every LLM exchange, deterministically, before any
spend. Treat it as a **cheap pre-filter, not a security boundary** — a deny-list of fixed phrases is
trivially rephrased around, so its honest job is dropping low-effort garbage before you pay for a
model call, not stopping a determined attacker. The real guarantee against a *successful* injection
is the structural invariant downstream (a poisoned `assembleSchedule` still can't satisfy
`noDoubleBooking`). Implement `UserInputGuardRail` (package
`com.embabel.agent.api.validation.guardrails`):

```java
public class RequestContentGuardRail implements UserInputGuardRail {

    @Override
    public String getName() { return "attendeeRequestGuard"; }

    @Override
    public String getDescription() {
        return "Screens attendee requests for a few known override markers (a cheap pre-filter)";
    }

    @Override
    public ValidationResult validate(String content, Blackboard blackboard) {
        // deterministic checks; return new ValidationResult(ok, errors)
    }
}
```

Attach it where raw input first meets a model, on the prompt runner:

```java
ai.withLlmByRole("cheapest")
  .withGuardRails(new RequestContentGuardRail())
  .creating(AttendeeProfile.class)
  .fromPrompt(...);
```

A failing validation raises `GuardRailViolationException` before the call is made. Because the
check is plain code, test it directly — no mock LLM needed (see `RequestContentGuardRailTest`).
`AssistantMessageGuardRail` is the same idea pointed at model *output*.

## Build and prove it bites

```bash
./mvnw -q verify
```

Write a test that proves the guardrail bites — a clashing draft never reaches the goal (the
`invoke` hits the budget instead of returning a clash). Use `FakeOperationContext` or
`EmbabelMockitoIntegrationTest`; never require a key.

---

For each annotation parameter (`pre`, `post`, `canRerun`, `@Condition(name=...)`) see the
[annotations reference](../reference/annotations.md); for `Budget` see the
[configuration reference](../reference/configuration.md). For *why* invariants live in the framework
rather than the prompt, see [About guardrails](../explanation/guardrails-and-invariants.md).
