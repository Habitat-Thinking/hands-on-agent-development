# Annotations

The Embabel annotations used in ConfPlanner, with their member attributes and where each first
appears. These are the typed surface the planner reads to derive a plan.

## `@Agent`

Marks a class as an agent. An agent supports **one** goal type and plans **only with its own
actions**.

| Member | Type | Description |
|---|---|---|
| `name` | `String` | Agent name shown in the planning log and shell. |
| `description` | `String` | Human-readable summary of what the agent achieves. |

## `@Action`

Marks a method as a planning action. Each action is a typed function: it *consumes* its parameter
types and *produces* its return type. The planner wires actions together by matching produced types to
consumed types.

| Member | Type | Description |
|---|---|---|
| `pre` | `String[]` | Names of conditions that must hold **before** the action can run. |
| `post` | `String[]` | Names of conditions promised **after** the action runs (a planning promise, not a runtime gate). |

!!! note
    A `post` condition on an `@AchievesGoal` action is a planning promise, not a runtime gate. To make
    an invariant bite, the goal must **require** it as a `pre` — see
    [guardrails and invariants](../explanation/guardrails-and-invariants.md).

## `@AchievesGoal`

Marks the single action whose output is the agent's goal — the destination the planner searches
backwards from.

| Member | Type | Description |
|---|---|---|
| `description` | `String` | What reaching this goal means. |

## `@Condition`

Marks a method that evaluates to a `boolean` over typed state. Conditions are referenced by name in
`pre`/`post` and are how [guardrails](../explanation/guardrails-and-invariants.md) are enforced.

| Member | Type | Notes |
|---|---|---|
| `name` | `String` | The name by which `pre`/`post` reference this condition (e.g. `@Condition(name = "noDoubleBooking")`). Defaults to the method name. |
| `cost` | `double` | Optional relative cost hint for evaluation ordering. |

## Tool groups

`@Action` has **no** `toolGroups` member. Add tool groups on the prompt runner instead, for example
`withToolGroup(CoreToolGroups.WEB)`.

## Where it connects

For the planning semantics behind these annotations, see
[actions, conditions, and goals](actions-conditions-goals.md) and
[About goal-oriented planning](../explanation/goap.md). To add a new action hands-on, see
[how to add an action](../how-to/add-an-action.md).
