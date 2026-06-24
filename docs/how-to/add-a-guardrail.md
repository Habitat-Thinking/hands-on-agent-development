# Add a guardrail

You want the framework — not a prompt instruction or a code review — to make a bad result
impossible: an invariant the goal must hold, a precondition before an action runs, and a budget so
the agent cannot run forever. This is the Lab 3 shape.

> **Key fact:** a `post` condition on an `@AchievesGoal` action is a *planning promise*, not a
> runtime gate. The goal is "achieved" as soon as the action produces its output type, whether or
> not the post still holds. To make an invariant bite, a **separate goal action must `pre`-require
> it**. The clean pattern is `assemble → confirm`.

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
