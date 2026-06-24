# Add a new typed action

You want to insert a new step into the pipeline and have GOAP route through it automatically — no
hand-wired call order. The planner derives the sequence from what each action *consumes* and
*produces*, so you add a typed producer and re-point whatever should consume it.

This is the Lab 2 shape: inserting a research step between shortlist and assemble.

## 1. Add the domain records the action moves

Domain types are immutable Java records under `domain/`. For a research step:

```java
public record SessionInsight(Session session, String whyRelevant, double matchScore) {}
public record ResearchedSessions(List<SessionInsight> insights) {}
```

Keep the id-only idiom: the model emits ids, plain code resolves ids back to `Session` from the
catalog.

## 2. Declare the `@Action` on the agent

Add the method to the agent class (e.g. `ConfPlannerAgent`). Its parameter types are what it
consumes; its return type is what it produces:

```java
@Action
ResearchedSessions researchSessions(CandidateSessions candidates, Ai ai) {
    return steps.research(candidates, ai);
}
```

Put the prompt body and id resolution in the shared `ConfPlanningCapabilities` `@Service` if more
than one agent will reuse it; keep the agent method a thin wrapper.

## 3. Re-point the consumer to the new type

Change the downstream action to consume the new type, so the planner *must* run the new step first:

```java
// was: DraftSchedule assembleSchedule(AttendeeProfile profile, CandidateSessions candidates, Ai ai)
DraftSchedule assembleSchedule(AttendeeProfile profile, ResearchedSessions researched, Ai ai) { ... }
```

Do **not** add a direct call to `researchSessions` from inside `assembleSchedule` — that hard-wires
the order and defeats GOAP. Let the types drive it.

## 4. Build and read the plan

```bash
./mvnw -q verify
./mvnw spring-boot:run
```

```text
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
```

The planning log should now read `… → shortlist → research → assemble`, with the new action between
the others purely because `research` produces what `assemble` now consumes.

## If you want the action to use a web tool

`@Action` has no `toolGroups` member. Add the tool group on the prompt runner:

```java
ai.withDefaultLlm().withToolGroup(CoreToolGroups.WEB).creating(T.class).fromPrompt(prompt);
```

`CoreToolGroups.WEB` is the string `"web"`. The tool group must be provided by an MCP tool server
at runtime; the default no-Docker lab path does not wire one.

## If the producer can fail and you want a clean replan

If the action may return `null` for a missing dependency, the planner marks the produces/consumes
link unsatisfied and replans rather than crashing. To make a step re-runnable as part of an
invariant loop, set `canRerun = true` — see [Add a guardrail](add-a-guardrail.md).

---

For the `@Action` / `@AchievesGoal` / `@Condition` parameters, see the
[annotations reference](../reference/annotations.md). For *why* goal-oriented planning beats a
hand-coded sequence, see [About GOAP planning](../explanation/goap.md).
