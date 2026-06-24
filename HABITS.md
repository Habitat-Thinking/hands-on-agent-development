# The eight-habit stack

The workshop is organised around eight habits. The first six map to a lab; the last two run
through all of them. Each habit has a runtime expression (in Embabel) and a build-time expression
(in the `ai-literacy-superpowers` harness) — same discipline, two altitudes.

| # | Habit | In ConfPlanner (runtime) | In the harness (build-time) | Lab |
|---|---|---|---|---|
| 1 | **Model the domain first** | Behaviour lives on `AttendeeProfile` (`PromptContributor`), not in scattered prompt strings (DICE) | Conventions and types are written down before code | 1 |
| 2 | **Name the goal, not the steps** | `@AchievesGoal`; GOAP derives the order. Add an action, the plan re-routes | You state intent; the orchestrator plans the steps | 2 |
| 3 | **Make the contract explicit** | `@Condition` invariants and preconditions the goal depends on; budgets; secured tools | Constraints in `HARNESS.md` the gate enforces | 3 |
| 4 | **Read the plan, not the vibes** | Planning log + Zipkin trace; diagnose a stuck plan from world-state | Read the orchestrator's plan and the diff before approving | 4 |
| 5 | **Right-size the model** | `withLlmByRole("cheapest"/"best")` per action; config, not code | `MODEL_ROUTING.md` routes agents by task complexity | 6 |
| 6 | **Extend by adding, not rewiring** | New capability = new types/actions/agent; existing flow unchanged | New behaviour = new constraint/agent, not a rewrite | 5 |
| 7 | **Test the seams** | Mock the LLM at the action boundary; assert on plan completion and invariants | Two-stage review and the constraint-gate test each change | all |
| 8 | **Govern the loop** | The whole cycle, made muscle memory: model → goal → guardrail → read → right-size → extend | The harness *is* the governed loop you run your engineering through | wrap |

## The ritual

Close every change — in either harness — the same way:

1. **Read the planning log.** What did the planner believe, and what did it choose?
2. **Read the trace.** What actually ran? Which condition stayed false?
3. **Confirm the acceptance check.** The framework, not a code review, says you're done.

## The operational ladder

How you drive the **build-time** harness, lowest to highest leverage. Push yourself up it as the
day goes on:

**Dictating → Commanding → Regulating → Orchestrating → Supervising.**

- *Dictating / Commanding* — you type or prompt each change (Tracks A/B).
- *Regulating* — you set the constraints and let the agent work inside them.
- *Orchestrating / Supervising* — the orchestrator plans and dispatches; you read the plan, gate the
  review, and confirm the acceptance check (Track C).
