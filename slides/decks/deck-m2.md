# Deck M2 — GOAP: name the goal, not the steps (10 slides)

> Shown before Lab 2. Habit badge: "Habit 2 — Name the goal, not the steps".
> Speaker notes: `../notes/m2-goap.md`.

## Slide 1 — Module title
- **Layout:** section title.
- **Visual:** plan ribbon with a gap between `shortlist` and `assemble` — a dotted empty slot.
- **On-slide copy:**
  - "M2 — GOAP: add an action, watch the plan grow"
  - "The data dependencies ARE the plan."

## Slide 2 — How-shaped vs what-shaped
- **Layout:** split comparison.
- **Visual:** left: imperative pseudo-code (`do A; then B; if C then D…`) tangled; right: a goal
  node + typed actions floating unordered.
- **On-slide copy:**
  - Headline: "Everything you've ever written says HOW. An agent should say WHAT."
  - Left caption: "Every ordering decision is a commitment you must revisit"
  - Right caption: "Declare consumes/produces; the order is a consequence"

## Slide 3 — This trick is 20 years old (and 50)
- **Layout:** timeline slide (the fun one).
- **Visual:** three era cards: 1971 STRIPS/Shakey (robot), 2005 F.E.A.R. (game soldier silhouette),
  today ConfPlanner (plan ribbon). Same precondition→effect arrows on each card.
- **On-slide copy:**
  - Headline: "GOAP: born in a research lab, proven in a shooter, now governing LLMs."
  - "1971 — STRIPS: preconditions, effects, world-state search (Fikes & Nilsson, SRI)"
  - "2005 — F.E.A.R.: typed actions + a planner made the era's best game AI (Orkin, GDC '06)"
  - "2026 — your `@Action`s, planned the same way"
- **Notes:** squad chatter was faked, the planning was real.

## Slide 4 — The mechanism
- **Layout:** diagram + log panel pair.
- **Visual:** backward-chaining diagram from `PersonalSchedule` goal leftward to `UserInput`;
  below it, the log panel showing the identical inferred plan line.
- **On-slide copy:**
  - Headline: "The planner searches backwards from the goal type."
  - "`assemble` needs `ResearchedSessions` → only `research` produces it → so `research` runs
    first"
  - Caption under log: "Same chain, printed at runtime. Nothing in the code says this order."

## Slide 5 — The proof: two edits, zero rewiring
- **Layout:** before/after log diff.
- **Visual:** two log panels; before: `…shortlist → assemble`; after: `…shortlist → research →
  assemble` with `research` highlighted green as an insertion.
- **On-slide copy:**
  - Headline: "Insert a step into the middle by only adding types."
  - "Edit 1: add `researchSessions(CandidateSessions) → ResearchedSessions`"
  - "Edit 2: make `assembleSchedule` consume `ResearchedSessions`"
  - Sub: "You reorder nothing. The plan re-routes itself."

## Slide 6 — Legible failure
- **Layout:** single behaviour card.
- **Visual:** `research` node returning `null`; planner icon looping back with "replan" arrow —
  not a crash icon.
- **On-slide copy:**
  - Headline: "`return null` → the planner replans. It doesn't crash, and it doesn't improvise."
  - Sub: "Compare: hard-wired pipeline → NPE. Model-driven loop → the model makes something up."

## Slide 7 — Why not let the LLM plan?
- **Layout:** claim + evidence (the spine slide).
- **Visual:** two-source convergence again (paper card + Rod Johnson card) onto the
  planner/actions split.
- **On-slide copy:**
  - Headline: "LLMs propose; planners dispose."
  - "GPT-4 solved ~12% of Blocksworld planning out of the box (Valmeekam et al., NeurIPS 2023)"
  - "The fix: an external sound planner/verifier — 'LLM-modulo' (ICML 2024). Embabel is that
    shape, productionised."
  - Citation line: "Johnson, 'AI for your Gen AI' · 'Building Reliable Agentic Systems, Pt I'"
- **Notes:** be honest about the trade vs ReAct-style flexibility.

## Slide 8 — GOAP is one of four
- **Layout:** four-up card row (NEW — 0.5.0 content).
- **Visual:** four planner cards: GOAP (lit, blue), Utility AI, Supervisor/LLM, `@State`
  machines (dimmed); one shared base labeled "same typed actions & conditions".
- **On-slide copy:**
  - Headline: "At 0.5.0, Embabel has four planner styles. Today you learn the most legible one."
  - "GOAP — fixed goal, derived path (today) · Utility — no fixed goal, maximise value ·
    Supervisor — LLM picks tools, types validate · `@State` — explicit machine, HITL"
  - Sub: "The discipline you're building is what all four plan over."
- **Notes:** current differentiator vs LangGraph; Lab 5 going-further picks one on purpose.

## Slide 9 — Foil + lab
- **Layout:** checklist card + task card combined.
- **Visual:** Track B chip; branch chip `lab2-before → lab2-after`.
- **On-slide copy:**
  - Forecast: "☐ Calls research from INSIDE assemble — hard-wiring the flow the planner owns"
  - Lab: "Add 2 records + 1 action → re-point `assembleSchedule` → read the plan diff → then
    `return null` and watch it replan"
  - Acceptance: "log reads `…shortlist → research → assemble` with no flow rewiring; you can
    explain the order from the types alone"
  - "Worksheet: `labs/lab2-goap.md` · 40 min"

## Slide 10 — Keystone + transition
- **Layout:** keystone table, row 2 highlighted; ritual footer.
- **On-slide copy:**
  - Row 2 lit: "Goal-oriented behaviour · GOAP re-derives the plan · Name the goal, not the steps"
  - Transition: "The planner routes through research now. It will also hand you a double-booked
    schedule with a straight face. Nothing yet says it CAN'T."
