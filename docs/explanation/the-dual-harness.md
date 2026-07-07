# About the dual harness

The most quietly radical idea in this workshop is that you are working inside *two* harnesses at
once, and they are teaching you the same thing. One harness governs the agent at runtime; the other
governs your engineering of that agent at build time. This page is about why that mirror exists, and
why it is the through-line rather than a coincidence.

## Two harnesses, one set of disciplines

A **harness** here means a structure that constrains and guides work so it stays safe, legible, and
on-goal — a planning loop with rules.

| | Build-time harness | Runtime harness |
|---|---|---|
| What it is | `ai-literacy-superpowers`: an orchestrator that dispatches an implementer, with `HARNESS.md` / `AGENTS.md` / `MODEL_ROUTING.md`, a constraint-gate, and two-stage review | Embabel: DICE domain models, GOAP planning, `@Condition` invariants, planning logs and traces |
| What it governs | the engineering of the agent | the agent itself |
| How you drive it | by climbing the operational ladder | by reading the plan and the trace, not the vibes |

The two harnesses share **three disciplines**, and the whole workshop is organised so you practise
each one twice:

1. **Context engineering.** At runtime: behaviour lives on typed domain objects that contribute
   their own context to prompts — [DICE](dice.md). At build time: conventions and types are written
   down, in `AGENTS.md` and `HARNESS.md`, before code.
2. **Architectural constraints.** At runtime: [GOAP](goap.md) derives flow from typed
   produces/consumes relationships; you never hard-wire order. At build time: a constraint-gate
   enforces "new capability via new types/actions only," "actions return typed records," and the
   rest — the same anti-rewiring rule, one altitude up.
3. **Guardrail design.** At runtime: invariants the goal pre-requires, preconditions, budgets,
   secured tools — [guardrails and invariants](guardrails-and-invariants.md). At build time: each of
   those rules is itself a `HARNESS.md` constraint with a test that proves it bites.

Read the table and the list together and a pattern emerges: every runtime mechanism has a build-time
twin. That is not decoration. It is the design.

## The operational ladder

You do not drive the build-time harness the same way all day. The **operational ladder** names five
rungs of increasing leverage, lowest to highest:

**Dictating → Commanding → Regulating → Orchestrating → Supervising.**

- *Dictating / Commanding* — you type each change, or prompt an agent change-by-change. This is
  Track A (by hand) and Track B (an ungoverned coding agent). Direct, but it does not scale and it
  does not protect you.
- *Regulating* — you stop describing steps and start setting *constraints*, then let the agent work
  inside them.
- *Orchestrating / Supervising* — the orchestrator plans and dispatches the work; your job becomes
  reading the plan, gating the review, and confirming the acceptance check. This is Track C.

The labs deliberately push you up the ladder as the day goes on: Lab 1 sits at Dictating/Commanding,
Lab 6 at Orchestrating. The point of the climb is leverage — the higher you stand, the more the
machinery carries, and the more your attention goes to *judging* rather than *typing*. Notice the
ladder is the build-time analogue of "name the goal, not the steps": at the top rungs you state
intent and let a planner derive the work, exactly as you do with [GOAP](goap.md) at runtime.

## The three tracks are three answers to "who does the work?"

Every lab can be done three ways, and the contrast is the teaching:

- **Track A — by hand.** Edit Java, run the shell, read the plan. The ground truth. Do this once so
  you can recognise when the higher tracks are lying to you.
- **Track B — ungoverned agent.** A teaching *foil*. Ask a coding agent to "add networking" with no
  constraints and watch it invent an untyped map, hard-wire flow order, or skip the test. The gap
  between what it does and what the harness would have required is the lesson.
- **Track C — through the harness (preferred).** The orchestrator plans the change, a Java
  implementer edits under `src/main/java/**`, and the constraint-gate plus two-stage review gate it.
  You supervise.

Track B is not there to make agents look bad; it is there to make *the harness* visible, by showing
what is missing when it is absent.

## The punchline: Track C builds the agent with the agent's own disciplines

Here is why the two harnesses are a mirror and not just an analogy. When you build ConfPlanner
through Track C, you are using context engineering, architectural constraints, and guardrail
design — to build an agent whose entire job is *context engineering, architectural constraints, and
guardrail design.* The orchestrator names a goal and lets a planning loop derive the steps, to build
an agent that names a goal and lets a planning loop derive the steps. **Track C builds the Embabel
agent using the very disciplines the Embabel agent itself embodies.** The harness is a planning loop
for your engineering, the way Embabel is a planning loop for the agent's behaviour.

That recursion is the through-line you are meant to *feel* by the end of the day, not just read. The
[eight habits](the-eight-habits.md) are the practical vocabulary for it, and the
[ritual](the-eight-habits.md) you close every change with — read the planning log, read the trace,
confirm the acceptance check — is the same in both harnesses on purpose.

To work the tracks, start with the [tutorials](../tutorials/index.md). For the harness constraints
and the ladder definitions as lookup, see the [reference](../reference/index.md); for driving Track
C concretely, the [how-to guides](../how-to/index.md).
