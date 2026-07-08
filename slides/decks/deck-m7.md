# Deck M7 — Wrap: govern the loop (10 slides)

> Shown 17:10–17:40. No lab. Land the recursion, give habits 7–8 their moment, send people home
> with a Monday plan. Speaker notes: `../notes/m7-wrap.md`.

## Slide 1 — Module title
- **Layout:** section title.
- **Visual:** the plan ribbon and the amber harness ring drawn as ONE closed loop for the first
  time.
- **On-slide copy:**
  - "M7 — Govern the loop"
  - "Two habits ran through everything. Now the loop closes."

## Slide 2 — The full stack
- **Layout:** the complete eight-habit table (first full reveal).
- **Visual:** HABITS.md table; rows 1–6 in their module colours, rows 7–8 spanning full width.
- **On-slide copy:** the eight-habit table verbatim, plus:
  - "Rows 1–6: one lab each. Rows 7–8: every lab."

## Slide 3 — Habit 7: test the seams
- **Layout:** evidence slide.
- **Visual:** the action boundary drawn as a seam line; mock chip (`FakeOperationContext`,
  `whenCreateObject`) on one side, real model chip on the other; a "0 API keys" badge on the
  test suite.
- **On-slide copy:**
  - Headline: "You built and tested an LLM agent all day with NO keys."
  - "The seam is the action boundary — mock the model, assert on plan completion and invariants"
  - "You test the deterministic shell, not the prose: `GuardrailEnforcementTest` asserts a
    REFUSAL"
  - Citation line: "Feathers, *Working Effectively with Legacy Code* (2004) — the seam, verbatim"

## Slide 4 — Habit 8: govern the loop
- **Layout:** cycle diagram.
- **Visual:** six-station loop: model → goal → guardrail → read → right-size → extend, with the
  ritual at the hub.
- **On-slide copy:**
  - Headline: "The whole cycle, made muscle memory."
  - Hub: "Read the planning log · Read the trace · Confirm the acceptance check"
  - Sub: "A checklist in the Gawande sense — externalised discipline, not memory."

## Slide 5 — The ladders, walked
- **Layout:** the M0 twin ladders, revisited with the day plotted on them.
- **Visual:** the two ladders from M0 slide 10. On the OPERATIONAL ladder, lab chips placed
  where each lab sat (Lab 1 at Dictating/Commanding, Lab 6 at Orchestrating) with a traced
  path arrow climbing through them — the day, plotted. On the COGNITIVE ladder, no path:
  instead a self-assessment prompt card hanging off it. The Build Gap caliper from M0 now
  reads "yours to measure".
- **On-slide copy:**
  - Headline: "The operational ladder — walked. The cognitive ladder — yours."
  - "You spent today climbing from Dictating to Orchestrating. That was the habitat's ladder."
  - "The take-home: score your three disciplines — context engineering, architectural
    constraints, guardrail design. The weakest one is your ceiling."
  - Sub: "The top rungs aren't more automation. They're more judgement per keystroke."
- **Notes:** the three-discipline scoring heuristic is from the cognitive ladder (repo
  `reference/cognitive-ladder.md`): the assessed level is the highest with substantial evidence
  across ALL three — strong specs with weak verification is L2, not L4.

## Slide 6 — The punchline
- **Layout:** the mirror, resolved (THE closing visual).
- **Visual:** the M0 dual-harness table transforming into two nested loops: the amber build-time
  loop wrapping the blue runtime loop, same three discipline labels on both.
- **On-slide copy:**
  - Headline: "Track C built the Embabel agent using the very disciplines the agent embodies."
  - "The orchestrator named a goal and let a planning loop derive the steps — to build an agent
    that names a goal and lets a planning loop derive the steps."
  - Sub: "The harness is a planning loop for your engineering."
  - Sub: "The two crafts from this morning — agentic building and building agents — were one
    loop all along."

## Slide 7 — Monday morning
- **Layout:** three action cards.
- **On-slide copy:**
  - Headline: "Three moves, this week, in YOUR codebase:"
  - "1 — Pick ONE invariant your system must never violate. Make it a condition your goal
    REQUIRES, with a test that proves it bites."
  - "2 — Add ONE trace surface: log what your orchestration BELIEVED, not just what it called."
  - "3 — Write ONE routing table: which model serves which step, justified by return type. One
    row is enough to start."

## Slide 8 — Where this is going
- **Layout:** horizon slide (NEW — state-of-play content).
- **Visual:** a road with three markers: 0.5.0 (you are here, lit) → 1.0 RC (near) → 2.0
  (horizon, hazy).
- **On-slide copy:**
  - Headline: "You learned on 0.5.0 'Darwin' — the current release. Here's the road."
  - "1.0.0-RC1 in flight: deprecated APIs already purged on `main` — re-verify before you re-pin"
  - "2.0 line: Spring Boot 4 · Spring AI 2.0 · Jackson 3"
  - "And beyond GOAP: Utility, Supervisor, `@State` planners · agentic RAG (`ToolishRag`) ·
    content guardrails · per-call cost tracking — the stretch worksheet `labs/lab7-rag.md` is
    your first step"
  - "And beyond one turn: **cross-turn memory** — `embabel-chat-store` persists a session, `@State`
    machines carry world-state across turns. ConfPlanner is deliberately single-turn; memory is the
    next shape, not a missing feature"
- **Notes:** the repo's risk register tracks this — point at the docs site. Two horizons on this
  slide, deliberately distinct: RAG shrinks the *within-a-call* window (the M6 reframe); memory
  extends the agent *across* calls. Both are named "where next," neither is in today's scope.

## Slide 9 — Where to go next
- **Layout:** resource grid.
- **Visual:** six link cards with QR codes if the venue suits.
- **On-slide copy:**
  - "This repo's docs site — tutorials → how-to → reference → explanation (and the risk
    register)"
  - "Embabel Guide — an MCP server that answers Embabel questions INSIDE Claude Code (SETUP §7)"
  - "Embabel Hub — hub.embabel.com · talk to the docs"
  - "`embabel/coding-agent` — what big looks like, dogfooded by the Embabel team"
  - "`embabel-agent-examples` — tracks snapshots: current best practice"
  - "Rod Johnson's essays — 'AI for your Gen AI' · 'Building Reliable Agentic Systems'"

## Slide 10 — Close
- **Layout:** final slide; keystone table fully lit, small, beneath two lines.
- **On-slide copy:**
  - "'The sovereign engineer is not the one who ignores AI, nor the one who surrenders to it.'"
  - "Read the plan. Read the trace. Confirm the check."
  - Keystone table (all six rows, earned)
  - "Thank you — go make failure legible."
  - Citation line: "Miles, *The Sovereign Engineer*"
- **Notes:** round-robin first (each table: one habit, one system) if time allows. The full
  quote if asked: "…the one who understands the architecture of the collaboration — amplifier
  and reference frame, power and direction, capability and judgment — and invests accordingly."
