# Habitat-thinking cheatsheet — the amber thread, module by module

> A presenter's companion. The workshop teaches **building agents** (the blue thread — Embabel:
> DICE, GOAP, guardrails, traces). This sheet carries the **agentic-building** craft alongside it
> (the amber thread — habitat engineering / habitat thinking, from *The Sovereign Engineer* and the
> [Habitat Thinking manifesto](https://github.com/Habitat-Thinking/manifesto)). Every runtime
> mechanism in the labs has a habitat-thinking twin; this is where you land it.
>
> **How to use it:** these are *optional colour*, not slides. Land **one or two** per module, in a
> sentence, tied to the thing on screen — then get back to the code. The whole point of the day is
> that the framing is *felt* in the labs, not lectured. Sources are cited so you can go deeper in
> Q&A: **[SE]** = *The Sovereign Engineer* (Russ Miles; chapter names from its contents);
> **[AMM]** = `reference/agentic-maturity-model.md`; **[CL]** = `reference/cognitive-ladder.md`;
> **[M]** = the manifesto.

---

## Reach-for-these — the core vocabulary (one line each)

- **Habitat engineering** — designing the working environment deliberately: *"the context they
  provide, the architectural boundaries they enforce, the feedback loops that catch drift before it
  compounds… they treat the collaboration space itself as an engineering artefact worthy of the
  same care they give to their code."* **[SE, Preface]** Those three things *are* the workshop's
  three disciplines, and *are* the dual harness.
- **The amplifier thesis** (DORA 2025) — *"AI is an amplifier. It magnifies whatever engineering
  discipline (or lack of it) already exists. Hand a powerful amplifier to a disciplined group and
  the music gets better and louder. Hand it to a band with no structure and you get noise at
  scale."* **[SE, Preface]** The whole day is about being the disciplined group.
- **The thesis in one line** — *"the quality of your AI-assisted work depends almost entirely on
  the quality of the collaboration space you design. Not on the model… On your creative, collective
  habitat."* **[SE, Preface]**
- **The three disciplines** — **context engineering** (encode accumulated wisdom into context),
  **architectural constraints** (make structural rules machine-checkable), **guardrail design**
  (feedback loops that catch drift). **[CL]** Same three, twice: build-time (harness) and runtime
  (Embabel).
- **Cognitive asymmetry** — *"human intelligence and artificial intelligence are fundamentally
  different kinds of cognition, not the same thing at different speeds."* **[SE, Preface]** You
  design the collaboration *because* the two minds are different.
- **Habitat, not cage** — a habitat makes the *desired* behaviour the path of least resistance; a
  cage makes *all* behaviour difficult. **[SE, "When Constraints Become Cages"]**
- **The two ladders + the Build Gap** — cognitive (Aware → Prompter → Verifier → Habitat Engineer →
  Specification Architect → Sovereign Engineer) **[CL]** and operational (Dictating → Commanding →
  Regulating → Orchestrating → Supervising) **[AMM]**. *Coherence, not raw level, is the signal*;
  the weakest of the three disciplines is the ceiling. **[CL]**
- **Sovereignty** — being *"the architect of the collaboration rather than a passive consumer of its
  outputs."* **[SE, Preface]** The take-home, not the entry fee.

## The manifesto, at a glance (quote it verbatim when it fits)

Purpose: **"Building places where every kind of intelligence in the room can think well together."**
Values (*we value X over Y*) and principles worth having on the tip of your tongue:

| Value (X over Y) | Lands loudest at |
|---|---|
| The whole cognition in the room over the code on the screen | M0 (dual harness), M7 |
| Sustained human understanding over the throughput of the place | M4 (read the plan), M7 |
| The living theory the inhabitants hold over the artefacts that embody it | M1 (rule on the type) |
| Designing the collaboration over directing the tool | M2 (name the goal) |
| Orientation that outlives the tools over tactics tuned to today's models | M0, M6 (roles not model ids) |
| Judgment that compounds over volume that impresses | M4, M6 (cheapest model that passes) |
| Accountability that rests with people over assurance manufactured by the place | M3 (the honest guardrail) |

Principles: *the habitat is the whole room, not the repository · optimise for orientation that
outlives the tools · understanding is a deliverable, not a bottleneck · agents keep understanding
cheap, people stay accountable · protect the friction that makes minds update · tend the place
deliberately, and often.* **[M, © 2026 Russ Miles, CC BY-SA 4.0]**

---

## M0 — Orientation · frame the day

*Where it lands:* the cold-open broken schedule, the dual-harness slide, the two ladders.

- **The pattern of panic.** Smart people do one of three things with AI — freeze, deny ("it's just
  autocomplete"), or capitulate (paste unread code to prod). All three share one cause: *no
  framework for what's actually happening.* The day is that framework. **[SE, "The Pattern of Panic"]**
- **Amplifier, not oracle.** Open with the amplifier thesis (above). It reframes "why agents fail
  in production" from a *model* problem to a *discipline* problem — which is why the fix is habits,
  not a bigger model. **[SE, Preface / "The Amplifier Thesis"]**
- **Two kinds of intelligence → two crafts.** The cognitive asymmetry is *why* there are two crafts
  (agentic building vs building agents) and why you design the collaboration at all. **[SE, "Two
  Kinds of Intelligence"]**
- **The habitat is the whole room, not the repository.** The dual harness is habitat thinking made
  concrete: the amber column *is* the collaboration space you design; the blue column is a *runtime*
  habitat around a model. *"An AI agent without a well-designed habitat is just model cognition with
  a toolbox."* **[M; SE]**
- **Coherence is the signal.** When you introduce the ladders, plant the Build Gap: a coherent L2/L2
  team beats an incoherent L4-thinker/L1-habitat one. Today climbs the *operational* rungs (by Lab 6
  you supervise); the *cognitive* climb is the take-home. **[CL, AMM]**

## M1 — DICE · Model the domain first  ·  discipline: **context engineering**

*Where it lands:* the avoid-list travelling into the prompt from `AttendeeProfile`.

- **DICE *is* context engineering.** *"Context engineering — how the team encodes accumulated wisdom
  into the agent's context."* **[CL]** The domain model is the curation mechanism; the prompt is
  *rendered* context, not the home of the rule.
- **The living theory over the artefacts that embody it.** The rule ("avoid vendor keynotes") is
  the *theory* the domain holds; putting it on the type means the theory travels with the data
  instead of being copied into artefacts (prompt strings) that drift. **[M]**
- **Surface what you already know.** DICE is the team surfacing tacit domain knowledge onto the type
  where the agent can't miss it — the same move as writing it into `CLAUDE.md` at build time.
  **[SE, "Surfacing What You Already Know"]**

## M2 — GOAP · Name the goal, not the steps  ·  from prompts → plans → specs

*Where it lands:* adding one action and watching the plan re-derive.

- **Designing the collaboration over directing the tool.** You declare what each step *needs and
  produces*; the planner directs the order. That is the manifesto value in Java: you design the
  space of moves, you don't puppet the moves. **[M]**
- **Up the input ladder.** Naming the goal is the L1→L3→L4 climb in what the agent takes as *input*:
  ad-hoc prompts → *plans* → *iteratively refined specs*. **[AMM]** GOAP makes "the plan is derived
  from typed intent" literally true.
- **Natural language fails at scale.** Hard-wiring step order in prose is the telephone game at
  machine speed; typed produces/consumes is *specification by example* the planner can't
  misread. **[SE, "The Communication Bottleneck"]**

## M3 — Guardrails · Make the contract explicit  ·  disciplines: **architectural constraints + guardrail design**

*Where it lands:* the `post`-doesn't-gate reveal; the assemble→confirm split; the content-guard honesty.

- **The constraint paradox — habitat vs cage.** A good constraint makes the *desired* path the
  easy one (the goal simply can't be reached with a clash); a cage makes *everything* hard. The
  assemble→confirm split is a habitat move, not a cage. Name it. **[SE, "Architecture as Guardrails"
  / "When Constraints Become Cages"]**
- **The architecture *is* the collaboration.** The invariant isn't bureaucracy bolted on — the
  structure you give the agent *is* how you collaborate safely with a fallible mind. **[SE, "The
  Architecture Is the Collaboration"]**
- **Accountability rests with people, not the place.** This is the honest-guardrail point (and the
  content-guard fix): a deny-list is *assurance manufactured by the place* — it looks like safety
  and isn't. The real guarantee is structural (the invariant a poisoned model can't reach), and a
  human still owns the contract. Don't let the place manufacture a false sense of safety. **[M]**

## M4 — Explainability · Read the plan, not the vibes  ·  discipline: **verification**

*Where it lands:* the STUCK plan; reading the world-state to name the false condition; Zipkin.

- **Sustained human understanding over the throughput of the place.** A stuck agent is *legible*:
  you read the world-state and name the condition that stayed false. Throughput without
  understanding is how teams ship slop at scale. **[M]**
- **The slop detector / the trust protocol.** "Read the plan, not the vibes" is slop detection at
  the plan level: output that *looks* right is the trap; the artefact (plan, trace) is the ground
  truth. **[SE, "The Slop Detector" / "The Trust Protocol"]**
- **The green-dashboard problem + the perception–reality gap.** Lab 4 makes you *name the root cause
  before touching code* — calibrating perception to reality, the skill the book calls out as one of
  the most important in the whole framework. **[SE, "Testing as Thinking" / "The Perception-Reality
  Gap"]**
- **Climb the observability ladder.** Eyeballs → captured → instrumented → aggregated → closed loop.
  The planning log is *instrumented*; the Zipkin trace moves you further up. **[AMM]**

## M5 — Extend without breaking · Extend by adding, not rewiring  ·  Track C, the parallel workflow

*Where it lands:* the second agent; the shared `@Service`; the schedule regression staying green.

- **The seam *is* the collaboration design.** Lifting the pipeline into a `@Service` so each agent
  keeps thin wrappers is the same habitability move as a well-placed module boundary — *can this be
  grown over years with comfort and confidence?* **[SE, "The Architecture Is the Collaboration";
  Gabriel's habitability question, Preface]**
- **Protect the friction that makes minds update.** The regression test that must stay green *is*
  the friction — it's what turns "I think I didn't break it" into "provably untouched." Don't
  optimise that friction away. **[M]**
- **Up the composition ladder.** Track C here is the operational climb from a single agent to a
  *primary + read-only critics* / *bounded ensemble* — the harness composes the ensemble, you
  supervise. **[AMM; SE, "The Parallel Workflow"]**

## M6 — Model routing · Right-size the model  ·  cost discipline + the cognitive substrate

*Where it lands:* `withLlmByRole`, the context-window-is-the-budget reframe, the local-model lever.

- **The attention budget.** The M6 reframe ("the window is the budget") is the book's *attention
  budget* — routing prices it, DICE fills it, guardrails cap it, RAG shrinks it. **[SE, "Cost
  Discipline in Practice" / "The Cache and the Attention Budget"; "The Twentieth Watt"]**
- **The depletion check.** Before spending the strong model on a step, ask what judgement it
  actually adds — the return-type heuristic is a depletion check with a Java face. **[SE, "The
  Twentieth Watt" / "The Depletion Check"]**
- **Orientation that outlives the tools.** Code commits to a *role*, config binds role→model —
  models churn quarterly, roles don't. That's the manifesto value made mechanical. **[M]**
- **The sovereignty dimension.** The local-model lever *is* sovereignty: route the step whose data
  can't leave the building to a substrate you own. **[SE, "The Cognitive Substrate" / "The
  Sovereignty Dimension"]**

## M7 — Wrap · Govern the loop  ·  the sovereign engineer

*Where it lands:* the eight-habit stack collapsing to one loop; the ritual; the eval lane; the horizons.

- **Tend the place deliberately, and often.** "Govern the loop" *is* the manifesto's closing
  principle — the habitat is not built once; it's tended. The ritual (read the plan · read the trace
  · confirm the acceptance check) is habitat maintenance in three lines. **[M; SE, "The Sovereign
  Engineer"]**
- **Understanding is a deliverable, and people stay accountable.** Trust as the six labs teach it is
  *legibility + bounded invariants* — sufficient while a human reads each plan. At the govern-the-loop
  horizon (no human reading every run), understanding has to be checked automatically: that's the
  **eval lane**, the answer to the green-dashboard problem. *"Agents keep understanding cheap; people
  stay accountable."* **[M; SE, "Testing as Thinking"]**
- **Sovereignty is the top rung, not the entry fee.** Close on the cognitive climb: today you drove
  the operational rungs; becoming the *architect of the collaboration* is the journey home. *"The
  habitat is ours to design. Let's build it well."* **[SE, Preface / "The Sovereign Engineer"]**

---

## The two through-lines to leave them with

1. **The dual harness is one idea twice.** Context engineering, architectural constraints, guardrail
   design — you practised all three at *runtime* (building the Embabel agent) and at *build-time*
   (Track C's harness building it). *You governed the build the way the thing you built governs its
   own reasoning.*
2. **The ritual is habitat-tending.** Read the plan, read the trace, confirm the acceptance check —
   in either harness. That's not a checklist; it's what keeping a good place to live *feels like*.

> Provenance for Q&A: the full framing is in the repo's `reference/` (`thesovereignengineer.pdf`,
> `agentic-maturity-model.md`, `cognitive-ladder.md`) and the
> [manifesto](https://github.com/Habitat-Thinking/manifesto). The M0 speaker notes
> (`m0-orientation.md`) carry the verbatim book quotes and citations; this sheet maps them onto
> where each one *lands* in the day.
