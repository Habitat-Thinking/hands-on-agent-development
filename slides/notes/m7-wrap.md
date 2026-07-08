# M7 — Wrap: govern the loop — topic notes & research

> Slot: 17:10–17:40. No lab. The job: consolidate eight habits into one loop, land the Track C
> punchline, and send people home with a Monday-morning plan.

## The job of this module

Two habits never got their own lab because they ran through all six: **test the seams** (7) and
**govern the loop** (8). The wrap gives them their moment, then lands the day's recursion: *you
built the Embabel agent using the very disciplines the Embabel agent embodies.* End with transfer —
what to do in their codebase this week.

## Slide beats

1. **The eight-habit stack, complete.** Show the full HABITS.md table for the first time (rows 1–6
   were revealed lab by lab). Walk habits 7–8:
   - *Test the seams* — every lab's acceptance check rode the mocked-LLM boundary
     (`FakeOperationContext`, `whenCreateObject`); the build was green all day with **no API
     keys**. The seam is the action boundary; assert on plan completion and invariants, not on
     prose.
   - *Govern the loop* — the whole cycle as muscle memory: model → goal → guardrail → read →
     right-size → extend.
2. **The ritual slide.** Read the planning log. Read the trace. Confirm the acceptance check.
   Same three steps closed every lab *and* every Track C change — one ritual, two harnesses.
3. **The ladders, walked.** Where each lab sat on the *operational* ladder: Lab 1 at
   Dictating/Commanding → Lab 6 at Orchestrating. Ask the room where they actually spent the
   day; most climbed a rung or two. Then hand them the *cognitive* ladder as the take-home:
   score your three disciplines (context engineering, architectural constraints, guardrail
   design) — **the weakest one is your ceiling** (strong specs with weak verification is L2,
   not L4 — `reference/cognitive-ladder.md`). The claim to leave them with: the higher rungs
   are not "more automation," they're **more judgement per keystroke** — attention shifts from
   typing to reading plans and gating reviews.
4. **The punchline slide (the through-line, cashed in).** Track C's orchestrator named a goal and
   let a planning loop derive the steps — to build an agent that names a goal and lets a planning
   loop derive the steps. Context engineering, architectural constraints, guardrail design: three
   disciplines, two altitudes, one loop. "The harness is a planning loop for your engineering."
5. **Monday morning slide** (make it concrete, three moves):
   - Pick **one invariant** your system must never violate; express it as a condition your goal
     *requires*, with a test that proves it bites.
   - Add **one trace surface**: log what your orchestration *believed*, not just what it called.
   - Write **one routing table**: which model serves which step, justified by return type — even
     if today it's one row.
6. **Where to go next.** The repo's docs site (Diátaxis: tutorials → how-to → reference →
   explanation), the Embabel repo/examples, and the gaps-and-extensions doc for what's landed in
   Embabel since the pinned 0.5.0.

## Research & references

- **Testing the seams.** Michael Feathers, *Working Effectively with Legacy Code* (2004) — the
  seam concept verbatim: a place to alter behaviour without editing code. The mocked-LLM action
  boundary is the seam that made "green with no keys" possible; that property is what let a *room
  full of laptops* run the same workshop without burning tokens — worth saying, it's also the CI
  story.
- **Test the contract, not the prose.** The suite asserts plan completion and invariants
  (`GuardrailEnforcementTest` asserts a *refusal*), never string-matching model output. This is
  the durable answer to "how do you test something non-deterministic": you test the deterministic
  shell around it. (Connect to Hoare/DbC from M3.)
- **Deliberate practice / habit framing.** The reason it's HABITS.md and not PRINCIPLES.md:
  principles are agreed with; habits are *performed*. The ritual is a checklist in the Gawande
  sense (*The Checklist Manifesto*, 2009) — expert performance under complexity comes from
  externalised discipline, not memory.
- **Governance context.** The day's artifacts — HARNESS.md constraints, MODEL_ROUTING.md,
  planning logs, invariant tests — are exactly the evidence set that AI-governance frameworks
  (NIST AI RMF; EU AI Act logging/oversight articles; ISO/IEC 42001 AI management systems) ask
  organisations to produce. Attendees didn't do compliance today, but they built the muscle that
  makes compliance a by-product instead of a project. Strong closing note for enterprise rooms.
- **The recursion is the retention hook.** People forget mechanisms; they remember structure.
  The dual-harness mirror (every runtime mechanism had a build-time twin) is the mnemonic the
  eight habits hang off — end on the two-column table, not on code.
- **The closing quote** (*The Sovereign Engineer*): *"The sovereign engineer is not the one who
  ignores AI, nor the one who surrenders to it. The sovereign engineer is the one who
  understands the architecture of the collaboration (amplifier and reference frame, power and
  direction, capability and judgment) and invests accordingly."* The close slide carries the
  first sentence; keep the rest in your pocket for the last question. Also from the book, if
  the room wants one more: the levels are concentric circles, not stacked blocks — *"nobody
  graduated"*; a Sovereign Engineer still writes prompts.

## Suggested closing move (5 min)

Round-robin: each table names (a) the habit they'll adopt first and (b) the system they'll apply
it to. Then the last slide: the keystone table one final time, now fully earned, with the ritual
underneath. "Read the plan. Read the trace. Confirm the check."

## Q&A themes to be ready for

- *"How much of this transfers off Embabel?"* — the habits and ritual entirely; the mechanisms
  have equivalents (conditions ≈ output validators/state machines; planning log ≈ decision
  logging; roles ≈ router layer). Embabel is the cleanest JVM expression, not the only one.
- *"What about multi-turn / memory / human-in-the-loop / RAG?"* — deliberately out of today's
  scope, but name the shape rather than deflecting. Two *distinct* horizons, and the "Where this is
  going" slide now carries both:
  - **RAG** shrinks the *within-a-call* window — the M6 reframe ("the window is the budget")
    cashed in: 500 sessions → the relevant handful. First step is the stretch worksheet
    `labs/lab7-rag.md` (runtime-verified against 0.5.0).
  - **Cross-turn memory** extends the agent *across* calls — a different window problem
    (state between turns, not budget within one). Embabel ships `embabel-chat-store` (0.3.4) to
    persist a session, and `@State` machines carry world-state across turns. ConfPlanner is
    single-turn *by design* — memory is the next agent shape, not a missing feature. HITL rides the
    same `@State` machinery.
  Point at the gaps-and-extensions doc (Gap 5 RAG, Gap 9 context/memory) and the Embabel examples
  repo for both.
- *"How do I sell the harness to my team?"* — start with Track B's foil on a real ticket: run the
  same change governed and ungoverned, diff the outcomes in review. Evidence over advocacy —
  which is, one last time, the day's habit.
