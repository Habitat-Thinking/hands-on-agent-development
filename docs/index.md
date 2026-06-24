# Hands-On AI Agent Engineering

Welcome to the **ConfPlanner** workshop — a hands-on course in engineering AI
agents you can actually trust in production. ConfPlanner is a conference-planning
agent built around a Goal-Oriented Action Planning (GOAP) core, and the workshop
walks you through building, testing, and operating it. The central idea is the
**dual harness**: a *build-time harness* that constrains how the agent's code is
written and changed, and a *run-time harness* that constrains how the agent
behaves once it is running — guardrails, invariants, and observability that keep
the agent inside the lines whether a human or another agent is driving it.

## How to use these docs

These docs follow the [Diataxis](https://diataxis.fr/) framework, which splits
documentation into four quadrants by the reader's need. Pick the section that
matches what you are trying to do right now.

- **[Tutorials](tutorials/index.md)** — *learning-oriented*. Step-by-step lessons
  that take you from zero to a working ConfPlanner. Start here if you are new.
- **[How-to guides](how-to/index.md)** — *task-oriented*. Recipes for getting a
  specific job done: running with a real model, enabling tracing, adding an
  action or guardrail, driving a change through the harness.
- **[Reference](reference/index.md)** — *information-oriented*. Precise technical
  descriptions of the domain model, configuration, catalog schema, and CLI.
  Consult it when you need exact details.
- **[Explanation](explanation/index.md)** — *understanding-oriented*. The why
  behind the design: DICE, GOAP, guardrails and invariants, the dual harness,
  the eight habits, and right-sizing models.

## Source code

The full source for ConfPlanner and this workshop lives on GitHub:
[russmiles/hands-on-agent-development](https://github.com/russmiles/hands-on-agent-development).
