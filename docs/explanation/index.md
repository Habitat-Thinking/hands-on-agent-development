# Explanation

Understanding-oriented material for ConfPlanner. These pages are for reading away from the
keyboard. They do not tell you which keys to press — the [tutorials](../tutorials/index.md) and
[how-to guides](../how-to/index.md) do that, and the [reference](../reference/index.md) holds the
exact facts. Here we step back and ask *why* the agent is shaped the way it is: why behaviour lives
on the domain object, why you name a goal instead of writing steps, why a postcondition alone does
not protect you, and why the workshop builds the agent with the very disciplines the agent itself
embodies.

- **[Domain-in-context engineering](dice.md)** — why behaviour belongs *on* the domain object as a
  `PromptContributor`, not scattered across prompt strings.
- **[Goal-oriented planning](goap.md)** — why you name the goal and let the planner derive the
  steps, and how adding one action re-routes the whole plan.
- **[Guardrails and invariants](guardrails-and-invariants.md)** — why a postcondition does *not*
  gate the goal in Embabel, and why an invariant must be a `@Condition` the goal pre-requires (the
  assemble → confirm pattern), alongside preconditions, budgets, and secured tools.
- **[The dual harness](the-dual-harness.md)** — the build-time and runtime harnesses, the three
  shared disciplines, the operational ladder, and why Track C is the punchline.
- **[The eight habits](the-eight-habits.md)** — the habit stack, and how each habit maps to a
  concrete Embabel mechanism and a lab.
- **[Explainability](explainability.md)** — why a goal-oriented agent is legible by construction: the
  planning log and trace *are* the explanation, not an add-on.
- **[The runtime harness](the-runtime-harness.md)** — how Embabel constrains the agent at run time,
  and how that mirrors the build-time harness.
- **[Extending the agent](extending-the-agent.md)** — why a new capability is a *new agent* over a
  shared `@Service`, since an agent plans only with its own actions.
- **[Right-sizing models](right-sizing-models.md)** — cost as a design parameter, routing by
  return-type complexity, and the local-model / data-residency angle.
