# About the runtime harness

ConfPlanner is built on two harnesses that mirror each other: a **build-time** harness that shapes how
code is written, and a **run-time** harness — the Embabel agent framework — that shapes how the agent
behaves once it is running. This page is about the run-time half, and why it matters even when you are
not calling a real model.

## What the runtime harness enforces

Embabel does not just call an LLM and hope. At run time it:

- holds the **typed contract** between actions, so no untyped `Map`/`Object` crosses an action
  boundary;
- derives the plan from those types via [goal-oriented planning](goap.md), rather than executing a
  hand-written sequence;
- enforces [guardrails and invariants](guardrails-and-invariants.md) as conditions over typed state,
  not as prompt instructions the model may ignore;
- records the planning log and trace so the run is [explainable](explainability.md) after the fact.

## Why mock mode still exercises the harness

It is tempting to think the harness only matters when a real model is in the loop. It does not. In
[mock mode](../how-to/use-mock-mode.md) the LLM responses are faked, but the planner, the typed
contracts, and the guardrails are all real. The shape of the run — the order of actions, the conditions
that must hold, the artefacts produced — is identical. That is precisely what lets the test suite stay
green with no API keys: the harness, not the model, defines the behaviour under test.

## Where it connects

The runtime harness is one half of [the dual harness](the-dual-harness.md); the build-time harness is
its twin one altitude up. To run the agent without a provider, see
[how to use mock mode](../how-to/use-mock-mode.md). For the keys that configure the runtime, see the
[configuration reference](../reference/configuration.md).
