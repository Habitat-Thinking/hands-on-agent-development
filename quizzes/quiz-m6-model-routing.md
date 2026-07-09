# Quiz — M6: Model routing (Lab 6)

> Section: **Model routing — the cheapest model that passes**. Habit: **Right-size the model.**
> Four short questions. Attempt them before opening the answers.

## Questions

1. The heuristic: what justifies routing `extractProfile` to `cheapest` and `assembleSchedule` to
   `best`? What property of each step is the honest proxy for how much judgement it needs?
2. Routing is "config, not code." Why doesn't re-routing every action break the build — and what does
   that tell you about what kind of decision routing *is*?
3. Why can the **mocked tests never show routing saving money**? And what is the *one keyless* way to
   watch routing bind to a **real** model with no cloud key?
4. Transfer / residency: which action would you route to a **local** model first in a regulated
   setting, and what field of the input drives that choice?

<details>
<summary>Answers &amp; discussion</summary>

1. **Return-type complexity.** `extractProfile` returns a flat list of fields pulled from a sentence —
   cheap work; `assembleSchedule` returns a reasoned, conflict-free schedule *plus* a rationale —
   real judgement. The return type is an honest proxy: a flat list of tags → `cheapest`; something
   carrying scores/rationale/a conflict-free arrangement → `best`.
2. `withLlmByRole("...")` commits to a *role*; `application.yml` binds role→model, and the tests mock
   the model — so changing the binding can't change behaviour. Routing is a **runtime cost/residency
   decision, not a correctness one**; that's precisely why re-routing your whole estate can never
   fail the build.
3. **The mock substitutes a canned response *before* the model is called — no tokens, no dollars — so
   there are no cost lines to compare** between `cheapest` and `best`; seeing routing *matter* needs a
   real key. The keyless way to at least watch routing bind to a *real* model: a **local model** (the
   `local` Spring profile pointing the OpenAI provider at Ollama) — no cloud key, nothing spent,
   nothing leaves the machine (it still can't show a *cost* delta).
4. *(Your answer.)* Usually the step that reads the rawest user data (here, `extractProfile`) — route
   exactly that on-prem, keep the rest in the cloud, and record the latency/quality-for-residency
   trade in `MODEL_ROUTING.md`.

</details>
