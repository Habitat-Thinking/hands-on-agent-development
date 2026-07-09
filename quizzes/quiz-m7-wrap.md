# Quiz — M7: Wrap + agentic RAG (Lab 7, take-home)

> Section: **Govern the loop** (and the take-home stretch, agentic RAG). Habits: **Govern the loop;
> Extend by adding.** Four short questions. Attempt them before opening the answers.

## Questions

1. The eight habits collapse into one repeatable loop. Name the three-line **ritual** that closes
   every change, in either harness.
2. Lab 7: what makes RAG *"agentic"* rather than plain RAG — and does adding it change the plan
   (`extract → loadCatalog → shortlist → research → assemble → confirm`)?
3. Trust as the six labs teach it is *legibility + bounded invariants* — enough while a **human reads
   each plan**. At the "govern the loop" horizon, where no human reads every run, what does trust then
   require, and which part of the repo now provides it?
4. Transfer: what is your organisation's `MODEL_ROUTING.md` today — i.e. where does the decision
   "which model/step does what, and why" actually live, and who can read it?

<details>
<summary>Answers &amp; discussion</summary>

1. **Read the planning log · read the trace · confirm the acceptance check.** What did the planner
   believe and choose; what actually ran and which condition stayed false; and the *framework*, not a
   code review, says you're done.
2. **The word "agentic" is the point:** plain RAG = *you* retrieve, then stuff the hits into the
   prompt; agentic RAG = you hand the model a **search tool** and it *decides* what to look up and
   when (Embabel's `ToolishRag`). The **plan is unchanged** — you swap the *implementation* of
   `shortlist` from "render a menu" to "search a store." That's habit 6 again: change how a step works
   without rewiring the flow.
3. **Automatic checking of *judgement*, not just legibility** — because "you could inspect it" isn't
   trust when nobody is inspecting. That's the **eval lane** (`./mvnw -Peval test`): a golden set
   scored by an LLM-as-judge, the complement to the deterministic keyless gates. (Deterministic gates
   for the seams; sampled evals for the judgement.)
4. *(Your answer.)* Usually the honest answer is "a Slack thread and a default constant." The habit is
   to make it a *reviewable artifact* — a table with a justification per role — so the decision can be
   read and defended.

</details>
