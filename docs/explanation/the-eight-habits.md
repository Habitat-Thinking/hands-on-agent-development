# About the eight habits

ConfPlanner is organised around eight habits. They are not slogans bolted onto the labs after the
fact; each habit *is* a lab's reason for existing, and each one has a concrete mechanism you can
point at in the code. This page steps back from the individual labs to ask what the stack is, why it
is in this order, and what holds it together.

## Why a stack, and why this order

The first six habits map one-to-one to a lab, and they are sequenced the way a real agent grows:

1. You **model the domain first**, because everything downstream reasons over those types.
2. Then you **name the goal** and let the planner derive the steps over those types.
3. Then you **make the contract explicit**, gating the goal with conditions over the same types.
4. Then you **read the plan** to understand and debug what the planner did.
5. Then you **right-size the model** behind each action.
6. Then you **extend by adding**, proving the seams were in the right place.

The last two are not stages but *postures* that run through all of the above: **test the seams** and
**govern the loop**. You cannot do them in one lab because they are how you do every lab.

Crucially, every habit has two expressions — a runtime one in Embabel and a build-time one in the
harness — because they are the same discipline at two altitudes. That doubling is the whole point of
[the dual harness](the-dual-harness.md).

## The stack

| # | Habit | Runtime mechanism (Embabel) | Build-time expression (harness) | Lab |
|---|---|---|---|---|
| 1 | **Model the domain first** | Behaviour lives on `AttendeeProfile` as a `PromptContributor`, not in scattered prompt strings — [DICE](dice.md) | Conventions and types written down before code | 1 |
| 2 | **Name the goal, not the steps** | `@AchievesGoal`; [GOAP](goap.md) derives the order; add an action and the plan re-routes | You state intent; the orchestrator plans the steps | 2 |
| 3 | **Make the contract explicit** | `@Condition` invariants the goal pre-requires, preconditions, budgets, secured tools — [guardrails](guardrails-and-invariants.md) | Constraints in `HARNESS.md` the gate enforces | 3 |
| 4 | **Read the plan, not the vibes** | Planning log + Zipkin trace; diagnose a `STUCK` plan from world-state | Read the orchestrator's plan and the diff before approving | 4 |
| 5 | **Right-size the model** | `withLlmByRole("cheapest"/"best")` per action — config, not code — [right-sizing](right-sizing-models.md) | `MODEL_ROUTING.md` routes agents by task complexity | 6 |
| 6 | **Extend by adding, not rewiring** | New capability = new types/actions/agent; existing flow unchanged | New behaviour = new constraint/agent, not a rewrite | 5 |
| 7 | **Test the seams** | Mock the LLM at the action boundary; assert on plan completion and invariants | Two-stage review and the constraint-gate test each change | all |
| 8 | **Govern the loop** | The whole cycle made muscle memory: model → goal → guardrail → read → right-size → extend | The harness *is* the governed loop you run your engineering through | wrap |

(Note the lab numbers do not run straight: habit 5, right-sizing, is taught in Lab 6, and habit 6,
extending, in Lab 5. The habit stack and the lab sequence are ordered for different reasons — the
stack for conceptual dependency, the labs for a satisfying build-up to the extend-without-breaking
capstone.)

## The two habits that hold the rest together

**Test the seams** is what makes the other habits *checkable*. ConfPlanner mocks the LLM at the
action boundary — `FakeOperationContext` for a single action, `EmbabelMockitoIntegrationTest` for a
whole plan — so the build is green with no API keys, and every guardrail has a test that proves it
bites (a clashing draft is asserted never to reach the goal). The seam is the action boundary, and
because the domain is typed and the flow is derived, the seams are exactly where the tests can grip.
A model you cannot test at the seam is a model you are trusting on faith.

**Govern the loop** is the habit the other seven add up to. It is the recognition that "model → name
the goal → guardrail → read → right-size → extend" is not a checklist you run once but a loop you run
continuously, until it becomes muscle memory. At runtime the loop is the agent's planning cycle; at
build time the harness *is* that governed loop, applied to your own engineering. This is the
recursion described in [the dual harness](the-dual-harness.md), named as a habit.

## The ritual that closes every change

Habits 4, 7, and 8 converge into a closing ritual you perform after every change, in *either*
harness:

1. **Read the planning log.** What did the planner believe, and what did it choose?
2. **Read the trace.** What actually ran? Which condition stayed false?
3. **Confirm the acceptance check.** The *framework* — not a code review, not a feeling — says
   you're done.

The ritual matters because it relocates the moment of trust. You do not approve a change because it
looks right; you approve it because the plan reads correctly, the trace matches, and the acceptance
check passes. That is what "read the plan, not the vibes" means in the body rather than on a slide.

To practise each habit, work the matching lab in the [tutorials](../tutorials/index.md). For the
operational ladder that the governing habit sits on, see [the dual harness](the-dual-harness.md). For
the constraints and commands as lookup, see the [reference](../reference/index.md).
