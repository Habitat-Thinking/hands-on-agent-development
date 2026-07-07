# M0 — Orientation: topic notes & research

> Slot: 09:00–09:40 (40 min talk, no lab). Sets the frame the whole day refers back to.
> Branch for demo: `main`. Next: M1 (Lab 1, DICE).

## The job of this module

Three things must land before anyone touches a keyboard:

1. **The problem is real** — agents fail in production for *structural* reasons, not model reasons.
2. **The dual harness** — the day teaches three disciplines twice, at runtime (Embabel) and at
   build time (`ai-literacy-superpowers`).
3. **The keystone table** — objective → mechanism → habit. Every module re-anchors to it.

If attendees leave M0 able to say *"we're learning context engineering, architectural constraints,
and guardrail design — twice"*, the module worked.

## Slide beats

1. **Cold open — a broken schedule.** Show (screenshot, don't run yet) a double-booked conference
   schedule an "agent" produced. Ask: whose fault is it — the model's, or the system that let the
   answer through? The day is about building systems where that question has a good answer.
2. **Why agents fail in production.** Four named failure modes, each of which maps to a lab:
   - *Ungrounded domain* — the model free-associates because nothing typed constrains it (→ Lab 1).
   - *Hidden steps* — flow is hard-wired and drifts; nobody can say why step 3 runs (→ Lab 2).
   - *No contract* — "please don't double-book" lives in a prompt, i.e. it is a request (→ Lab 3).
   - *No trace* — when it breaks, the only debugging tool is vibes and re-runs (→ Lab 4).
3. **The industry context slide** (see research below): autonomy is cheap, *reliability* is the
   scarce good. One line: "a 95%-reliable step, run 10 times in sequence, is a 60%-reliable plan."
   (0.95^10 ≈ 0.599 — do the arithmetic live, it lands.)
4. **Two crafts hide inside "AI agents"** — the disambiguation the field skips. *Agentic
   building*: AI agents as your **workforce** — agentic workflows, harnesses, habitat thinking.
   *Building agents*: the agent as your **product** — typed actions, planning, guardrails;
   that's Embabel. Most confusion (in conference talks, in tooling debates, in procurement)
   comes from treating these as one topic. Then the declaration: **today is mostly BUILDING
   AGENTS — driven via agentic building** (Track C is the former operating on the latter).
5. **Habitat thinking, 60–90 seconds** (from *The Sovereign Engineer*, chs. 2–3 — quote it
   verbatim, the lines are better than paraphrase): *"A habitat is a persistent, evolving
   collaboration environment"* with four components — conventions that encode accumulated
   wisdom, architectural constraints that give the AI boundaries it cannot intuit, test suites
   that provide the metacognition the AI lacks, and feedback loops that catch reference-frame
   drift before it compounds. The bridge line to the whole day: *"An AI agent without a
   well-designed habitat is just model cognition with a toolbox"* — the habitat provides the
   cognitive architecture the agent itself lacks. That is true of the coding agents in Track C
   AND of ConfPlanner: Embabel's typed actions, conditions and budgets are a *runtime* habitat
   around a model. Guard against the cage misreading: *"a habitat makes the desired behaviour
   the path of least resistance; a cage makes all behaviour difficult."* Don't book-talk it —
   land the definition and the bridge line, then move; the dual-harness slide cashes it in.
6. **What Embabel is.** A JVM agent framework by Rod Johnson (creator of Spring). Its bet:
   **the LLM does the judgement inside actions; a deterministic planner (GOAP) does the control
   flow.** Contrast with the ReAct-style loop where the model decides the next step each turn.
7. **The dual harness table** (from README). Emphasise: not an analogy for decoration — every
   runtime mechanism in the labs has a build-time twin you will also use *today*. With slide 4
   in place this now has names: the amber column is the agentic-building craft, the blue column
   is the building-agents craft — same three disciplines.
8. **Two ladders.** The five verbs (Dictating → Commanding → Regulating → Orchestrating →
   Supervising) are the *agent-behaviour* dimension of the **Agentic Experience 5-level habitat
   maturity model** (TechTalk) — the *operational* read: what the habitat delivers, L1–L5
   across 14 dimensions. Alongside it sits the book's **cognitive ladder** (L0–L5 personas:
   Aware → Prompter → Verifier → Habitat Engineer → Specification Architect → Sovereign
   Engineer) — what the team can think and do. The signed difference is the **Habitat Build
   Gap**, and *coherence, not raw level, is the signal*: a coherent L2/L2 team is healthier
   than an L4-cognitive/L1-operational one. Both source docs live in the repo's `reference/`.
   For M0, plant only: today's labs climb the operational verbs (by Lab 6 you supervise); the
   cognitive climb is the take-home. One more nuance worth saying aloud: the levels are an
   expanding repertoire, not a graduation — a Sovereign Engineer still writes prompts.
9. **Keystone slide** — the objective → mechanism → habit table. Say explicitly: "we will return to
   this slide six times."
10. **Logistics.** Branch contract (`labN-before`/`labN-after`, `lab4-broken`), the three tracks,
    `./mvnw verify` is green with *no keys*, and the closing ritual: **read the planning log, read
    the trace, confirm the acceptance check.**

## Live demo script (5 min)

```bash
git checkout main
./mvnw spring-boot:run
# in the shell:
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule" -p
```

Point at, in order: (1) the **inferred plan** printed before execution — "nobody wrote this
sequence"; (2) the world-state lines flipping conditions to TRUE; (3) the final schedule with its
rationale. Don't explain the mechanics — that's the day. The demo's only job is to make the
planning log an object of curiosity.

## Research & references (for slide credibility)

- **Deterministic planner + LLM actions.** Rod Johnson's launch essay *"Embabel: A new agent
  platform for the JVM"* (Medium, 2025), plus *"AI for your Gen AI: How and Why Embabel Plans"*
  and *"Building Reliable Agentic Systems, Part I"* (both Medium) — the planning argument in his
  own words. Press framing for the slide: The New Stack, *"Spring creator wants Java's type system
  to tame agentic AI"*. Core quote-shape: the plan should be explainable *before* it runs.
- **LLMs are poor planners — the academic line.** Kambhampati et al., *"On the Planning Abilities
  of Large Language Models"* (NeurIPS 2023) and *"LLMs Can't Plan, But Can Help Planning in
  LLM-Modulo Frameworks"* (ICML 2024): LLMs generate plausible plans, not valid ones; pair them
  with an external verifier/planner. Embabel's GOAP-plus-typed-actions is a production-shaped
  instance of exactly this "LLM-modulo" architecture — worth one slide, it gives the design
  academic legs.
- **Workflows vs agents.** Anthropic, *"Building Effective Agents"* (Dec 2024): most production
  value comes from *workflows* — structured, verifiable orchestration — rather than fully
  autonomous loops. Embabel sits deliberately on the structured side while keeping goal-directed
  flexibility (the plan re-derives; it isn't a hard-coded DAG).
- **The compounding-error arithmetic.** The 0.95^n slide. Any multi-step agent argument should
  include it; it's the cleanest justification for framework-enforced invariants (Lab 3) and
  budgets over "just prompt better."
- **Regulatory tailwind.** EU AI Act (in force Aug 2024, GPAI obligations phasing in from
  Aug 2025) requires logging and traceability for high-risk systems; NIST AI RMF asks for
  "traceable and transparent" behaviour. The planning log + trace ritual is compliance-shaped, not
  just hygiene — say this once here, cash it in properly at M4.
- **Spring lineage.** Rod Johnson, *Expert One-on-One J2EE Design and Development* (2002) →
  Spring. The relevance: Embabel repeats the Spring move — take an over-ceremonied space
  (EJB then; agent frameworks now) and replace it with POJOs + annotations + dependency
  injection. Java-shop audiences feel this immediately; it's the "why JVM, why not LangChain"
  answer.
- **Habitat engineering / *The Sovereign Engineer*** (Russ Miles, Habitat-Thinking; Leanpub,
  2026 edition — the PDF and two distilled reference docs live in the repo's `reference/`).
  What the slides quote verbatim: the habitat definition and its four components (conventions /
  architectural constraints / test suites-as-metacognition / feedback loops); *"an AI agent
  without a well-designed habitat is just model cognition with a toolbox"*; the
  habitat-vs-cage line; the amplifier thesis (*"AI does not replace engineering judgment. It
  amplifies whatever engineering judgment you already have"* — backed in ch. 1 by DORA 2025
  and a 150-developer lifecycle study: creation-phase speed vs maintenance-phase cost). The
  book's shift, in its own triplet: from optimising the input to designing the habitat; from
  crafting clever prompts to engineering coherent collaboration spaces; from AI-as-chat-partner
  to the working environment as an engineering artefact. Deep lineage for Q&A: Richard
  Gabriel's "is this code a good place to live?" (via Christopher Alexander) — the book extends
  it: if code is a habitat for programmers, the whole development environment is a habitat for
  the combined intelligence of humans and AI. Note: the book's term is **habitat engineering**;
  "habitat thinking" is the umbrella/org name (Habitat-Thinking) — both are the speaker's own
  vocabulary, use whichever fits the sentence. The three disciplines on the dual-harness slide
  (context engineering, architectural constraints, guardrail design) are the book's — the
  `ai-literacy-superpowers` harness in Track C is the framework made runnable. The
  two-crafts framing (agentic building vs building agents) is the speaker's conference
  vocabulary rather than a book chapter title — it operationalises the book's distinction
  between designing the collaboration and being its product.
- **The two ladders' provenance** (both files in `reference/`): the operational read is the
  **Agentic Experience 5-level habitat maturity model** (TechTalk) — 14 dimensions, L1–L5; the
  workshop's five verbs are its agent-behaviour dimension, and its testing dimension
  (Manual → Asserting → Verifying → Validating → Assuring) is a nice Q&A depth card for habit
  7. The cognitive ladder (L0–L5) and the **Habitat Build Gap** (`cognitive_level −
  habitat_maturity_mean`; coherent within ±0.5; "ambition outpaces enablement" above;
  "inherited habitat" below) are from the AI Literacy framework / the book. Scoring heuristic
  worth quoting: the weakest of the three disciplines is the ceiling — strong specs with weak
  verification is L2, not L4.

## Anticipated questions

- *"Is this a Claude Code / coding-agent workshop?"* — no: that's the agentic-building craft,
  and you'll *use* it (Track C) as the workforce. The subject being taught is the other craft —
  building agents (Embabel). The point of naming both up front is so nobody spends the morning
  expecting the other workshop.
- *"Why not LangChain / LangGraph / Spring AI directly?"* — Spring AI is the model-access layer
  Embabel builds **on**; LangGraph makes you draw the graph, Embabel derives it from types. The
  differentiator is the deterministic planner over typed actions, plus JVM-native DI, testing, and
  security.
- *"Is GOAP overkill vs. just calling tools in a loop?"* — the loop *is* a planner, just an
  implicit, unexplainable one running inside the model. GOAP makes the same decision inspectable
  and testable. (Cross-ref M2 and M4.)
- *"Do we need Docker / keys?"* — no key to build and test; one key to run against a real model;
  Docker only for Zipkin in Lab 4 (optional path).

## Transition out

"Every failure mode on slide 2 has a mechanism and a habit. First failure mode: the ungrounded
domain. Check out `lab1-before`."
