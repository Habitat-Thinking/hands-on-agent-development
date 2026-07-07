# M2 — GOAP: name the goal, not the steps — topic notes & research

> Slot: 10:50–11:50 (15 min frame + demo, 40 min lab, 5 min debrief).
> Branches: `lab2-before` → `lab2-after`. Habit: **Name the goal, not the steps.**

## The job of this module

Make attendees *feel* that the plan is derived, not written. The proof move: drop `researchSessions`
into the middle of the pipeline by only adding types — the plan re-routes itself. Secondary lesson:
failure is legible — `return null` and the planner *replans* instead of crashing.

## Slide beats

1. **How-shaped vs what-shaped code.** Everything they've ever written says *how*: do this, then
   that. An Embabel agent says *what*: here is the goal; here is what each action consumes and
   produces. "The data dependencies **are** the plan."
2. **GOAP's pedigree — the F.E.A.R. slide** (the fun one, use it). GOAP was invented by Jeff Orkin
   at Monolith for the game *F.E.A.R.* (2005); GDC 2006 paper *"Three States and a Plan: The A.I.
   of F.E.A.R."*. Enemy soldiers were praised as the best game AI of their era — not because each
   behaviour was scripted, but because soldiers had typed actions (flip table → cover exists) and
   a planner chained them. Twenty years later the same trick governs LLM actions. Roots go back
   further: **STRIPS** (Fikes & Nilsson, SRI, 1971) — preconditions, effects, world-state search —
   the Shakey-the-robot planner. One slide: 1971 → 2005 → today.
3. **The mechanism in one diagram.** Backward search from the goal type: `PersonalSchedule` needs
   `confirmSchedule`, which needs a `DraftSchedule`, which needs `ResearchedSessions`… until you
   reach `UserInput`, the thing you actually have. Show the inferred plan line from the log next to
   the diagram — same thing.
4. **The proof: insert a step without reordering anything.** Two edits only: add the typed action;
   change what `assembleSchedule` consumes. Zero flow code. Show the before/after planning-log
   diff.
5. **Replanning = legible failure.** `return null` from research → the planner marks the
   produces/consumes link unsatisfied and replans. Contrast with a hard-wired pipeline (NPE or a
   silent skip) and with a model-driven loop (the model improvises something).
6. **Why not let the LLM plan?** One slide on the Kambhampati line (below) + Embabel's position:
   judgement inside actions, control flow in a deterministic planner. This slide is the
   intellectual heart of the day — give it 3 minutes.
7. **Keystone table callback** — row 2.

## Live demo script

```bash
git checkout lab2-before
./mvnw spring-boot:run
x "…" -p            # capture the plan: extract → loadCatalog → shortlist → assemble
git checkout lab2-after
x "…" -p            # plan now: … shortlist → research → assemble — nobody reordered anything
```

Then the replanning beat: live-edit `researchSessions` to `return null;`, re-run, and narrate the
planner replanning rather than crashing. Revert. (Rehearse this — it's the demo most likely to
surprise *you*.)

## Research & references

- **Jeff Orkin, "Three States and a Plan: The A.I. of F.E.A.R."** (GDC 2006, MIT Media Lab page
  hosts the PDF). The canonical GOAP source; the "squad chatter was faked, the planning was real"
  anecdotes are excellent speaker colour.
- **STRIPS: Fikes & Nilsson (1971), *"STRIPS: A New Approach to the Application of Theorem Proving
  to Problem Solving"*** — preconditions/add/delete lists; the world-state formalism the planning
  log's TRUE/FALSE lines descend from.
- **LLMs as planners — the negative results.** Valmeekam & Kambhampati et al., *"On the Planning
  Abilities of Large Language Models"* (NeurIPS 2023): GPT-4 solved ~12% of Blocksworld
  out-of-the-box in their harness; and *"LLMs Can't Plan, But Can Help Planning in LLM-Modulo
  Frameworks"* (ICML 2024): the fix is an external sound verifier/planner in the loop. Embabel is
  the LLM-modulo shape productionised: GOAP is the sound planner; LLMs fill typed actions.
- **Rod Johnson on LLM planning.** *"AI for your Gen AI: How and Why Embabel Plans"* and
  *"Building Reliable Agentic Systems, Part I"* (Medium) argue the same from the practitioner
  side — an agent whose flow you cannot predict is an agent you cannot test, budget, or certify.
  Pair quote with the academic result — theory and practice agreeing is a strong slide.
- **GOAP is one of four planners now.** As of 0.5.0 Embabel documents GOAP (A*, replans after
  every action) alongside a Utility AI planner, a Supervisor/LLM planner, and `@State` typed
  state machines — one programming model across all four. Today teaches GOAP because it's the
  most legible; flag the others in one breath here and point at the gaps-and-extensions doc
  (this is Embabel's current headline differentiator vs LangGraph).
- **The contrast class: ReAct** (Yao et al., 2022) and model-in-the-loop orchestration — the model
  picks the next step each turn. Not a straw man: it's more flexible in open worlds. The trade:
  flexibility vs. explainability/testability. Embabel picks the second for enterprise work; be
  honest about the trade and the room will trust the rest of the day.
- **"Dead capability costs nothing."** The going-further exercise (add an action nothing consumes;
  planner never schedules it) demonstrates capability-vs-plan separation — akin to a service in a
  registry nobody calls. It reframes "adding actions" as safe, which is Lab 5's foundation.

## Track B foil — predict out loud

The ungoverned agent asked to "add a research step" will likely *call* research from inside
`assembleSchedule` — hard-wiring the exact flow the planner exists to own. Name it before the lab:
"if your agent writes `researchSessions(...)` inside another method body, that's the anti-pattern."

## Debrief prompts

- Read someone's planning-log diff aloud: where did `research` appear, and what two edits caused it?
- Ask: what does the plan search *not* know about? (Costs, quality, latency — the planner routes on
  types and conditions. Foreshadows both budgets (M3) and model routing (M6).)

## Transition out

"The planner now routes through research. But it will also happily hand you a double-booked
schedule with a straight face — nothing yet says it *can't*. Guardrails next."
