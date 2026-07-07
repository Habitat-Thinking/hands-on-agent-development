# About explainability

A trustworthy agent is one whose reasoning you can inspect. ConfPlanner is built so that every run
leaves two readable artefacts behind: the **planning log** and the **trace**. This page is about why
those artefacts matter and how to read them as a model of the agent's mind.

## The planning log is the plan made visible

Because the agent uses [goal-oriented planning](goap.md), the order of actions is *derived*, not
written. The planning log is where that derivation becomes legible:

```
UserInput → extractAttendeeProfile → loadCatalog → shortlistSessions → researchSessions
          → assembleSchedule → confirmSchedule
```

Reading it top to bottom tells you exactly which capabilities the planner chose and in what order —
and, when a run stalls, *which* required type was never produced. The log is the first place to look
when behaviour surprises you.

## The trace is the plan executed

Where the planning log shows intent, the trace shows what actually happened: each action's inputs and
outputs, the LLM calls made, the time and tokens spent. With [Zipkin tracing](../how-to/enable-zipkin-tracing.md)
enabled, the same information is rendered as a span tree you can click through.

## Why two artefacts, not one

The gap between *what was planned* and *what executed* is where most agent bugs live. Keeping the two
views separate — the derived plan and the recorded execution — lets you ask precise questions: did the
planner choose the wrong path, or did a chosen action behave wrongly? That distinction is the heart of
debugging an agent you can trust.

## Where it connects

Explainability is the observable face of the [dual harness](the-dual-harness.md): the trace is to
run-time what the planning log is to design-time. To turn on the optional distributed trace, see
[how to enable Zipkin tracing](../how-to/enable-zipkin-tracing.md). For the configuration keys that
control trace output, see the [configuration reference](../reference/configuration.md).
