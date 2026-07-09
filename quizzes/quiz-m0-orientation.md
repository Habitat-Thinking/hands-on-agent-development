# Quiz — M0: Orientation

> Section: **Orientation**. Frame for the day: *read the plan, not the vibes.*
> Four short questions. Attempt them before opening the answers.

## Questions

1. M0 names four reasons agents fail in production, and each maps to a later lab. Name **two** of the
   four failure modes, and the habit that answers each.
2. "Two crafts hide inside 'AI agents'." What are the two crafts, and which one is today *mostly*
   about — and which one are you *using* to do it?
3. The dual harness teaches the same **three disciplines** at two altitudes (build-time and runtime).
   Name the three disciplines.
4. A step that is 95% reliable, run 10 times in sequence, produces a plan that is roughly how
   reliable? What does that number argue *for*?

<details>
<summary>Answers &amp; discussion</summary>

1. **Ungrounded domain → *model the domain first* (Lab 1); hidden steps → *name the goal, not the
   steps* (Lab 2); no contract → *make the contract explicit* (Lab 3); no trace → *read the plan, not
   the vibes* (Lab 4).** The point of M0 is that these are *structural* failures, not model failures —
   which is why habits, not a bigger model, fix them.
2. **Agentic building** (AI agents as your *workforce* — agentic workflows, harnesses, habitat
   thinking) vs **building agents** (the agent as your *product* — Embabel). Today is mostly *building
   agents*, driven via *agentic building* (that's Track C: the harness operating on the product).
3. **Context engineering, architectural constraints, guardrail design.** Every runtime mechanism in
   the labs has a build-time twin that teaches the same discipline.
4. **≈ 60%** (0.95¹⁰ ≈ 0.599). It argues for **framework-enforced invariants and budgets** over "just
   prompt better" — compounding error means reliability has to be structural, not hoped for.

</details>
