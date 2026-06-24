# About extending the agent

Once ConfPlanner can build one schedule, the natural next step is to make it do *more* — a second
piece of behaviour over a second goal. This page is about the reasoning behind how that extension is
structured, and the trap it is designed to avoid.

## One agent, one goal

An Embabel `@Agent` supports **one** goal type and plans **only with its own actions**. That
constraint is deliberate: it keeps each agent's plan space small and its planning log readable. When
you want a second behaviour — say, a networking plan alongside the schedule — you do *not* bolt a
second `@AchievesGoal` onto the same class.

## Share logic, not goals

The way to extend is to put shared capability in a plain `@Service` and let each agent call it. A
second `@AchievesGoal` in the same class, or an `@EmbabelComponent` whose actions you hope will be
pooled into another agent's plan, does **not** work — those actions are not pulled into the other
agent's planning. The clean seam is an ordinary Spring service that both agents depend on.

## Why this keeps the agent trustworthy

Every action you add widens the plan space the planner must search and the trace you must read. Keeping
one goal per agent, and sharing logic through services rather than through extra goals, means each
agent stays explainable: its planning log shows exactly the actions it owns, in the order the types
demand.

## Where it connects

Extension is [goal-oriented planning](goap.md) applied a second time, under the same
[guardrails](guardrails-and-invariants.md) and the same [runtime harness](the-runtime-harness.md). To
do this hands-on, Lab 5 walks through adding a second goal — see the
[tutorials](../tutorials/index.md). For the action and goal annotations involved, see the
[actions, conditions, and goals reference](../reference/actions-conditions-goals.md).
