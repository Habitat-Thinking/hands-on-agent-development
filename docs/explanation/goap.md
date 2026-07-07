# About goal-oriented planning

Most code you have written tells the computer *how*: first do this, then that, then the other. An
Embabel agent is built the opposite way. You declare *what* you want — a goal — and what each action
consumes and produces, and a planner derives the *how* for you. This page is about why that trade is
worth making, and what it feels like in practice.

## You name the goal; the planner finds the path

Embabel uses **GOAP** — Goal-Oriented Action Planning, an idea borrowed from game AI. You never
write the sequence of steps. Instead:

- Each `@Action` is a typed function: it *consumes* some types and *produces* another. In
  ConfPlanner, `shortlistSessions` consumes an `AttendeeProfile` and a `SessionCatalog` and produces
  `CandidateSessions`.
- One action is marked `@AchievesGoal`. That is the destination.
- The planner searches backwards from the goal: to produce the goal's output it needs *that* type,
  which means it needs the action that produces it, which needs *its* inputs, and so on, until it
  reaches the input you actually have (a `UserInput`).

The order you see in the planning log —

```
UserInput → extractAttendeeProfile → loadCatalog → shortlistSessions → researchSessions
          → assembleSchedule → confirmSchedule
```

— is not written anywhere. It is *derived* every run from the produces/consumes relationships among
the types. Nothing in the code says "research comes after shortlist." It comes after shortlist
because research consumes `CandidateSessions`, which only shortlist produces. The data dependencies
*are* the plan.

## Why give up writing the steps?

Hand-wired control flow is a liability that grows. Every `if`, every ordering decision, every "and
then call X" is a commitment you must revisit when requirements change. The promise of GOAP is that
**the order is a consequence, not a commitment.** Change what an action consumes or produces and the
plan re-derives itself. You maintain a set of typed capabilities; the planner maintains the wiring.

This also changes how failure looks. When an LLM application built on hand-written flow misbehaves,
the temptation is to poke the prompt and re-run on a hunch. When a GOAP agent cannot reach its goal,
it goes `STUCK` in a *legible* way: the world-state tells you exactly which condition or type never
became available. A derived plan is a readable plan — which is the whole subject of
[reading the plan](the-eight-habits.md), and the reason the failing-plan lab exists.

## The proof: add an action, watch the plan re-route

The clearest way to feel that the plan is derived rather than coded is to insert a step into the
middle of an existing flow *without touching any ordering*.

In an early version of ConfPlanner, `assembleSchedule` consumed `CandidateSessions` directly. To add
a research step you do only two things:

1. Add a typed action `researchSessions(CandidateSessions) → ResearchedSessions`.
2. Change `assembleSchedule` to consume `ResearchedSessions` instead of `CandidateSessions`.

You reorder nothing. Yet the next run plans `… → shortlist → research → assemble`, with the new
action slotted *between* the other two. Why? Because `assemble` now needs a `ResearchedSessions`,
and only `research` produces one, so `research` must run first — and `research` needs
`CandidateSessions`, which `shortlist` still produces. The planner re-routed through the new
capability on its own. That is the second habit, **name the goal, not the steps**, made visible.

The inverse is just as instructive: if you make `researchSessions` return `null`, the planner does
not crash. It observes that the produces/consumes link is unsatisfied and *replans*, rather than
charging ahead. Legible failure again.

## The constraint that makes this trustworthy

GOAP only earns your trust if the planner reasons honestly about availability. ConfPlanner's
build-time harness encodes a matching rule: *never hard-wire flow order; new capability is added via
new types/actions/agents only.* A change that secretly calls one action from inside another — say,
invoking research from within assemble — would defeat the planner and is exactly the anti-pattern the
harness review catches. The discipline at runtime (let the planner route) and the discipline at
build time (don't smuggle in hidden order) are the same discipline; see
[the dual harness](the-dual-harness.md).

## Two limits worth knowing

GOAP's power has edges, and two of them shape ConfPlanner's design hard enough that they deserve to
be understood, not just memorised:

- **An agent plans only with its own actions.** A second agent cannot borrow another agent's actions
  for planning. This is *why* the networking capability is a separate agent that re-declares its
  upstream steps as thin wrappers over a shared service — covered in
  [extending the agent](extending-the-agent.md) and the extend lab.
- **A postcondition is a planning promise, not a runtime gate.** The planner believes an action's
  `post` will hold; it does not re-check it to decide the goal is done. That single fact is the most
  important nuance in the whole workshop and has its own page —
  [guardrails and invariants](guardrails-and-invariants.md).

To *do* the add-an-action exercise yourself, follow Lab 2 in the [tutorials](../tutorials/index.md).
For the planning-log flags (`-p`, `-r`) and the annotation members, see the
[reference](../reference/index.md).
