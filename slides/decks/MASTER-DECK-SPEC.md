# Master deck spec — Hands-On AI Agent Engineering (complete handover)

**To Claude Design:** this single document specifies **eight slide decks (85 slides)** for a
full-day conference workshop. Build them as eight separate decks sharing ONE design system
(Part 1). Part 2 gives per-slide specs: layout, a **Graphic** instruction, verbatim on-slide
copy, and a speaker cue. Deliver in deck order M0 → M7.

**The governing preference: diagram first, words second.** Every slide that *can* carry a
graphical depiction of its idea *must* — the copy captions the graphic, never the reverse. Where
a slide is unavoidably textual (task cards, the keystone table), typography and layout do the
visual work. If a Graphic instruction under-specifies something, resolve it in favour of the
clearest possible depiction of the *mechanism* being taught, using the vocabulary in Part 1. Do
not add decorative imagery to fill space; a slide with one strong diagram and one sentence is the
house style.

| Deck | Title | Slides | Shown |
|---|---|---|---|
| M0 | Orientation | 14 | 09:00 opening talk |
| M1 | DICE: behaviour on domain objects | 9 | before Lab 1 |
| M2 | GOAP: name the goal, not the steps | 10 | before Lab 2 |
| M3 | Guardrails: make the contract explicit | 11 | before Lab 3 |
| M4 | Explainability: debug by reading state | 11 | after lunch, before Lab 4 |
| M5 | Extend without breaking | 10 | before Lab 5 |
| M6 | Model routing | 11 | before Lab 6 |
| M7 | Wrap: govern the loop | 10 | 17:10 close |

---

# Part 1 — Design system (applies to all eight decks)

## Audience & tone
Senior Java/JVM engineers at a full-day workshop. Confident, spare, a little wry. No stock
imagery, no robots, no glowing brains, no clip-art AI. Dark backgrounds (bright rooms, dark
code). Every visual metaphor must be *mechanical* (flows, gates, meters, seams, loops) — never
mystical.

## Typography
- Headlines: one strong grotesque (Inter/Söhne class), tight, sentence case.
- Code, log lines, type names: true monospace (JetBrains Mono class). Type names like
  `AttendeeProfile` are ALWAYS monospace, even inside headlines and diagram labels.
- Body text minimal: max ~4 bullets a slide; bullets are full short sentences, not fragments.

## Color semantics (roles are fixed; exact hues may flex)
- **Electric blue** = the runtime harness (Embabel: actions, plans, conditions).
- **Warm amber** = the build-time harness (`ai-literacy-superpowers`: orchestrator, constraints,
  review). Whenever both harnesses appear: blue left/inner, amber right/outer.
- **Green** = a condition satisfied / `TRUE` / a passing test. **Red** = unmet / `FALSE` /
  refused. These two are *semantic only* — never decorative.
- Neutral slate greys for structure; one near-white for headline text.
- Dimmed/desaturated = "not yet taught" or "not chosen".

## Recurring components (design once, reuse everywhere)
1. **Plan ribbon** — the pipeline `UserInput → extract → loadCatalog → shortlist → research →
   assemble → confirm` drawn as a horizontal chain of typed nodes (rounded rectangles joined by
   solid arrows; arrows always mean "produces → consumes"). Nodes light blue when a module
   activates them; unlit nodes stay slate. This is the workshop's signature graphic — it appears
   on every module title slide showing that module's slice lit.
2. **Keystone table** — the 6-row objective → mechanism → habit table. Ends every deck with the
   current row highlighted, earlier rows lit, later rows dimmed. Same pixel geometry every time
   so the room sees ONE table filling up across the day.
3. **Log panel** — a styled terminal block for planning-log excerpts: dark card, monospace,
   subtle window chrome, `TRUE` tokens green and `FALSE` tokens red. Must read as a living
   terminal, not a screenshot.
4. **Habit badge** — small numbered chip top-right of every content slide in M1–M6
   (e.g. "Habit 3 · Make the contract explicit").
5. **Ritual footer** — closing slides: "Read the planning log · Read the trace · Confirm the
   acceptance check" as three small icons (scroll / spans / checkmark) with labels.
6. **Track chips** — A / B / C markers: A "by hand" (neutral), B "ungoverned agent" (red-tinged),
   C "through the harness" (amber).
7. **Forecast checklist** — the Track B prediction card: a betting-slip styled card with empty
   checkboxes, used before every lab and scored at the debrief.

## Graphics directives (the "good depiction" rules)
- **One idea, one diagram.** Each diagram depicts exactly the mechanism named in the headline.
  If a spec lists several elements, compose them into a single coherent scene, not a collage.
- **Arrows have one meaning per diagram** (stated in the spec): data flow, causality, or
  time — never mixed, never "relates to".
- **State is shown as state**: conditions as diamond gates (green/red), budgets as meters,
  guardrails as physical gates/walls, retries as loops that visibly terminate at a meter.
- **Comparisons are side-by-side twins** with identical geometry so the *difference* is the only
  thing that differs (before/after logs, belt/braces lanes, cloud/building).
- **Timelines run left→right** with era cards of equal size; "you are here" markers lit.
- **Code cards** show real code, syntax-highlighted, trimmed to the teaching line(s); highlight
  at most two tokens per card (green for the fix/addition, red for the fault).
- **Tables are typeset, not drawn as images**; row-highlighting does the emphasis.
- Labels in diagrams: max 3 words where possible; monospace for anything that exists in code.
- Contrast: all text ≥ WCAG AA on the dark ground; green/red states also differ in shape
  (✓/✗ or filled/hollow) so color is never the sole carrier.

## Slide furniture
Module code (M0–M7) bottom-left; slide number bottom-right; no logos except title slides.
Citations render as one muted caption line at the slide's foot, monospace-free.

---

# Part 2 — The decks

## Deck M0 — Orientation (14 slides)

### M0.1 — Title
- **Layout:** full-bleed title.
- **Graphic:** dark field; the plan ribbon rendered faint and unlit as a horizontal motif behind
  the title — the day's map, not yet awake.
- **Copy:**
  - "Hands-On AI Agent Engineering"
  - "Habits for safe, explainable, domain-grounded AI"
  - "ConfPlanner · a goal-oriented Embabel agent · Java 21"
- **Cue:** shown while attendees run `./mvnw clean verify`.

### M0.2 — Cold open: whose fault is this?
- **Layout:** single artifact + one question.
- **Graphic:** a conference-schedule card (day grid, three time slots) with TWO session tiles
  stacked into the same 10:00 slot, their overlap glowing red; the rest of the card calm and
  plausible. The wrongness must be visible from the back row.
- **Copy:**
  - "An agent built this."
  - "Whose fault is the double-booking — the model's, or the system that let the answer through?"
- **Cue:** don't answer; the day answers.

### M0.3 — Why agents fail in production
- **Layout:** 2×2 grid.
- **Graphic:** four cells, each one minimal icon + lab chip: an untethered balloon (ungrounded
  domain), tangled wires (hidden steps), a handshake drawn in dotted line (no contract), a black
  box (no trace).
- **Copy:**
  - "Ungrounded domain — nothing typed constrains the model → Lab 1"
  - "Hidden steps — flow is hard-wired; nobody can say why step 3 runs → Lab 2"
  - "No contract — 'please don't' lives in a prompt → Lab 3"
  - "No trace — debugging = vibes and re-runs → Lab 4"
- **Cue:** each cell is a module.

### M0.4 — The arithmetic of hope (and the amplifier)
- **Layout:** one huge equation, one thesis line.
- **Graphic:** `0.95¹⁰ ≈ 0.60` set enormous; beneath it a 10-link chain where each successive
  link is drawn slightly more transparent, the last links barely there (arrows = sequence).
  Below, a small amplifier glyph — one triangle, two labelled signal paths passing through it:
  "discipline in → excellence out" and "mess in → wreckage out". Same amplifier, opposite
  outcomes.
- **Copy:**
  - "A 95%-reliable step, ten times in a row, is a 60%-reliable plan."
  - "AI does not replace engineering judgment. It amplifies whatever engineering judgment you
    already have." — the amplifier thesis
  - "DORA 2025: high performers improved across the board; low performers had their problems
    amplified. The variable was the practices, not the tool."
  - Caption: "Miles, *The Sovereign Engineer* ch. 1 · DORA 2025"
- **Cue:** do the arithmetic out loud; the amplifier thesis is why structure — not model
  choice — is the day's subject.

### M0.5 — Two crafts hide inside "AI agents"
- **Layout:** side-by-side twins with a verdict bar beneath.
- **Graphic:** two panels with IDENTICAL geometry so the parallel reads instantly. Left panel
  (amber): a human figure at the top of a small ladder supervising a crew of agent chips
  assembling a structure — agents as *workforce*. Right panel (blue): a single agent box on an
  engineering bench — typed ports, planner gear, gate diamond — the agent as *product*. Beneath
  both panels, one full-width verdict bar carrying the declaration.
- **Copy:**
  - "'AI agents' is two different crafts. Name which one you're doing."
  - Left: "AGENTIC BUILDING — agents as your workforce: agentic workflows, harnesses, habitat
    thinking"
  - Right: "BUILDING AGENTS — the agent as your product: typed actions, planning, guardrails
    (Embabel)"
  - Verdict bar: "Today is mostly BUILDING AGENTS — and you'll drive it VIA agentic building."
- **Cue:** most confusion in this space comes from talking about these two crafts as one;
  naming them is the day's first gift.

### M0.6 — Habitat thinking, in one slide
- **Layout:** definition slide with one central diagram.
- **Graphic:** a habitat boundary drawn around a human figure and agent chips working together —
  explicitly an *environment*, not a cage: the boundary built from FOUR labelled bricks (the
  book's own components) — "conventions — accumulated wisdom, encoded" · "architectural
  constraints — boundaries the AI cannot intuit" · "test suites — the metacognition the AI
  lacks" · "feedback loops — catch drift before it compounds" — with open gates through it.
  Outside the boundary, small and dismissed, a crossed-out speech bubble labelled "prompting
  harder".
- **Copy:**
  - "Stop optimising the prompt. Design the habitat."
  - "'A habitat is a persistent, evolving collaboration environment.'"
  - "'An AI agent without a well-designed habitat is just model cognition with a toolbox.' The
    habitat provides the cognitive architecture the agent itself lacks."
  - "A habitat makes the desired behaviour the path of least resistance. A cage makes all
    behaviour difficult. You'll live in one all day: the Track C harness IS a habitat."
  - Caption: "Miles, *The Sovereign Engineer* — habitat engineering, chs. 2–3"
- **Cue:** 60–90 seconds, not a book talk. The "model cognition with a toolbox" line is the
  bridge: true of your coding agents AND of ConfPlanner — the day builds a runtime habitat
  (typed actions, conditions, budgets) around a model. M0.9 cashes this in.

### M0.7 — What Embabel is
- **Layout:** split definition.
- **Graphic:** left: an LLM chip drawn *inside* a typed-action box (blue outline labelled
  `@Action`, with typed input/output ports) — the model contained by a contract. Right: a planner
  box emitting the ordering arrows that join several such actions. One composition: judgement
  inside the boxes, flow between them.
- **Copy:**
  - "Embabel: the LLM does the judgement; a deterministic planner does the flow."
  - "Typed `@Action`s — the model works inside a contract"
  - "GOAP planner — the order is derived, not written"
  - "By Rod Johnson (creator of Spring) · JVM-native · Spring DI, testing, security"
  - Caption: "Johnson, 'Embabel: A new agent platform for the JVM' · The New Stack interview"
- **Cue:** the EJB→Spring rhyme.

### M0.8 — Not just our opinion
- **Layout:** two sources converging.
- **Graphic:** an academic paper card (left) and a practitioner essay card (right), both with
  arrows converging onto a miniature of the M0.7 architecture. Arrows = endorsement.
- **Copy:**
  - "Theory and practice agree: don't let the model be the planner."
  - "Kambhampati et al.: LLMs generate plausible plans, not valid ones — pair them with a sound
    planner ('LLM-modulo', ICML 2024)"
  - "Anthropic, *Building Effective Agents*: most production value is structured workflows, not
    free-running loops"
- **Cue:** 3 minutes; the intellectual spine.

### M0.9 — The dual harness
- **Layout:** two-column mirror (THE motif slide).
- **Graphic:** amber column left (build-time), blue column right (runtime); three horizontal
  discipline bars bridging both columns at equal heights, like rungs joining two rails. The
  symmetry IS the message — make the two columns visibly mirror-images in structure.
- **Copy:**
  - "You will work inside two harnesses today. They teach the same three disciplines."
  - Bridging bars: "Context engineering · Architectural constraints · Guardrail design"
  - Left header: "Build-time — the agentic-building craft, governing your engineering"
  - Right header: "Runtime — the building-agents craft: Embabel, governing the agent"
- **Cue:** the M0.5 distinction becomes architecture here; promise the punchline for 17:00.

### M0.10 — Two ladders (and the gap between them)
- **Layout:** twin vertical ladders + a caliper.
- **Graphic:** two parallel ladders, same height, side by side. Left ladder, header
  "OPERATIONAL — what your habitat delivers": five rungs, Dictating → Commanding → Regulating →
  Orchestrating → Supervising, Track chips A/B pinned low, C spanning the top two. Right
  ladder, header "COGNITIVE — what you can think and do": six rungs, the personas — Aware →
  Prompter → Verifier → Habitat Engineer → Specification Architect → Sovereign Engineer.
  Between the ladders, a caliper instrument measuring the vertical distance between two
  "you are here" markers, its readout labelled "the Habitat Build Gap". Height = leverage on
  both ladders.
- **Copy:**
  - "Two ladders: what you can think, and what your habitat delivers."
  - "Today's labs climb the OPERATIONAL rungs — by Lab 6 you're supervising, not typing."
  - "The COGNITIVE climb is the take-home — sovereignty is the top rung, not the entry fee."
  - "The gap between the two is the diagnostic. Coherence — not raw level — is the signal."
  - Caption: "Agentic Experience 5-level maturity model (TechTalk) · the cognitive ladder,
    Miles, *The Sovereign Engineer* — both in the repo's `reference/`"
- **Cue:** don't teach the 14 dimensions; plant the two-reads idea and the Build Gap word. The
  levels are an expanding repertoire, not a graduation — a Sovereign Engineer still writes
  prompts.

### M0.11 — The keystone
- **Layout:** full-slide table (recurring component #2), all rows lit.
- **Graphic:** the keystone table itself, generous type, lab chips right-aligned per row.
- **Copy (verbatim table):**
  - "1 · Typed domain models (DICE) · `PromptContributor` on the domain · Model the domain first"
  - "2 · Goal-oriented behaviour · GOAP re-derives the plan · Name the goal, not the steps"
  - "3 · Preconditions & invariants · `@Condition` gate + budget + secured tool · Make the
    contract explicit"
  - "4 · Explainability · planning log + Zipkin trace · Read the plan, not the vibes"
  - "5 · Extend without breaking · new agent/action, flow unchanged · Extend by adding, not
    rewiring"
  - "6 · Right-size the model · `withLlmByRole` cheap-vs-strong · Right-size the model"
- **Cue:** "we return to this slide six times."

### M0.12 — How the day works
- **Layout:** three horizontal bands.
- **Graphic:** band 1: the branch walk as a subway-style line with stations
  `lab1-before → lab1-after → … → lab4-broken → lab4-after → … → main`, `lab4-broken` station
  marked red; band 2: the three track chips; band 3: the ritual footer icons.
- **Copy:**
  - "Operate git branches: `-before` → worksheet → diff against `-after`"
  - "Track A by hand · Track B ungoverned agent (the foil) · Track C through the harness"
  - "`lab4-broken` is the one branch that fails `verify` — on purpose"
  - "Read the planning log · Read the trace · Confirm the acceptance check"
- **Cue:** logistics fast; worksheets carry detail.

### M0.13 — Live demo
- **Layout:** demo interstitial, near-empty.
- **Graphic:** the log-panel component, empty, cursor blinking — anticipation as design.
- **Copy:**
  - "`x \"I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a
    schedule\" -p`"
  - "Nobody wrote the sequence you're about to see."
- **Cue:** make the planning log an object of curiosity. Keyless venue: run
  `SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run` and invoke with `plan "…"` — `x` needs a live
  model (it ranks the request against goals via the LLM) and fails under the mock profile; `plan`
  invokes the goal directly and prints the plan with no flags (`docs/how-to/use-mock-mode.md`).

### M0.14 — Transition
- **Layout:** single line.
- **Graphic:** the M0.3 failure grid with cell 1 (untethered balloon) now lit and the others
  dimmed.
- **Copy:**
  - "First failure mode: the ungrounded domain."
  - "`git checkout lab1-before`"

---

## Deck M1 — DICE: behaviour on domain objects (9 slides)
*Habit badge on all content slides: "Habit 1 · Model the domain first".*

### M1.1 — Module title
- **Layout:** section title.
- **Graphic:** plan ribbon with `extract` and `shortlist` nodes lit blue, rest slate.
- **Copy:**
  - "M1 — DICE: behaviour on domain objects"
  - "The prompt is rendered context, not the home of the rule."

### M1.2 — The anti-pattern: prompt-string drift
- **Layout:** three stacked code cards, deliberately untidy.
- **Graphic:** three prompt-string cards fanned at slightly different angles (disorder is the
  point), each containing a *different paraphrase* of "don't recommend vendor content" with the
  differing words highlighted; a red rubber-stamp across them: "WHICH ONE IS CURRENT?"
- **Copy:**
  - "The same rule, three homes, three wordings."
  - "Duplication you can't grep for — the copies are paraphrases, not tokens."
- **Cue:** DRY violation, worse than code duplication.

### M1.3 — DICE
- **Layout:** definition + one code panel.
- **Graphic:** a record card for `AttendeeProfile` drawn as a labelled box of typed fields; a
  small megaphone icon attached to it; the `contribution()` method line glowing green inside.
  The card visibly "speaks" a sentence-ribbon that flows off toward a prompt outline.
- **Copy:**
  - "Domain In Context Engineering — put the rule ON the type that owns it."
  - Code: `record AttendeeProfile(...) implements PromptContributor { String contribution() {
    … "AVOID these topics: …" } }`
  - "The record doesn't just store the avoid-list. It knows how to speak about it."
- **Cue:** the rule travels with the data.

### M1.4 — You already believe this
- **Layout:** lineage bridge.
- **Graphic:** two book-spine cards ("Evans — Domain-Driven Design, 2003", "Fowler — Anemic
  Domain Model") with arrows into the M1.3 record card. Arrows = "this idea, extended".
- **Copy:**
  - "Behaviour-rich domain objects — the prompt is just a new client."
  - "Anemic record = data bag = the anti-pattern you already avoid in business logic"
  - Caption: "Evans 2003 · Fowler 2003 · Anthropic, 'Effective context engineering' 2025"

### M1.5 — Belt and braces
- **Layout:** two-lane diagram.
- **Graphic:** one source node `avoidTopics` fanning into two parallel lanes with identical
  geometry: lane "BRACES" → `contribution()` → prompt outline, drawn with a **dashed** arrow
  (asks); lane "BELT" → `shouldAvoid()` filter gate → menu, drawn with a **solid** arrow
  (guarantees). Dashed-vs-solid is the entire visual argument; add a small legend.
- **Copy:**
  - "One rule, one owner, two enforcement surfaces."
  - "Braces: `contribution()` asks the model"
  - "Belt: the code filter makes vendor sessions unreachable — even under prompt injection"
- **Cue:** foreshadows Lab 3's asking-vs-enforcing.

### M1.6 — Keep the model's job small
- **Layout:** side-by-side twins.
- **Graphic:** left twin (red-tinged): model emitting a heavy full-`Session`-JSON block; right
  twin (green-tinged): model emitting only `["s-104","s-217"]` + a reasoning line, then a code
  gear resolving ids against a catalog cylinder. Identical geometry; only the payload differs.
- **Copy:**
  - "The id-only idiom: the model does judgement, code does the rest."
  - "Narrow surface → fewer hallucinated fields → cheaper models can own more steps (Lab 6)."
- **Cue:** `resolve()` drops invented ids — the type system as bouncer.

### M1.7 — Predict the foil
- **Layout:** forecast checklist (component #7).
- **Graphic:** betting-slip card with Track B chip and three empty checkboxes.
- **Copy:**
  - "Track B forecast — score the ungoverned agent against this:"
  - "☐ Invents an untyped `Map` instead of a record field"
  - "☐ Puts the rule in one prompt string, not on the type"
  - "☐ Skips the test"
- **Cue:** score at debrief.

### M1.8 — The lab
- **Layout:** task card.
- **Graphic:** branch chip `lab1-before → lab1-after`; four numbered step-chips in a row.
- **Copy:**
  - "Add `avoidTopics` → give the profile a voice (`PromptContributor`) → extract it → honour it
    (attach + filter)"
  - "Acceptance: avoided tags absent from the schedule · avoid-list visible in the prompt log
    (`-p`) · filtered-shortlist test green"
  - "Worksheet: `labs/lab1-dice.md` · 40 min"
- **Cue:** the acceptance check is wiring, not fields.

### M1.9 — Keystone + transition
- **Layout:** keystone table, row 1 highlighted; ritual footer.
- **Copy:**
  - Row 1 lit.
  - "The rule has a home. Who decided the ORDER the actions run in? Nobody. That's next."

---

## Deck M2 — GOAP: name the goal, not the steps (10 slides)
*Habit badge: "Habit 2 · Name the goal, not the steps".*

### M2.1 — Module title
- **Layout:** section title.
- **Graphic:** plan ribbon with a dotted empty slot gaping between `shortlist` and `assemble` —
  a socket waiting for a node.
- **Copy:**
  - "M2 — GOAP: add an action, watch the plan grow"
  - "The data dependencies ARE the plan."

### M2.2 — How-shaped vs what-shaped
- **Layout:** side-by-side twins.
- **Graphic:** left twin: imperative pseudo-code rendered as a tangled flowchart (nested
  branches, crossing arrows — visual spaghetti); right twin: a goal star + typed action tiles
  floating *unordered*, no arrows at all. The absence of arrows on the right is the point.
- **Copy:**
  - "Everything you've ever written says HOW. An agent should say WHAT."
  - Left caption: "Every ordering decision is a commitment you must revisit"
  - Right caption: "Declare consumes/produces; the order is a consequence"

### M2.3 — This trick is 20 years old (and 50)
- **Layout:** timeline (the fun slide).
- **Graphic:** three equal era cards left→right: 1971 (Shakey-the-robot silhouette, "STRIPS"),
  2005 (game-soldier silhouette behind a flipped table, "F.E.A.R."), 2026 (the plan ribbon,
  "ConfPlanner"). The SAME small precondition→effect arrow motif repeated on all three cards —
  the visual thesis is "one mechanism, three eras".
- **Copy:**
  - "GOAP: born in a research lab, proven in a shooter, now governing LLMs."
  - "1971 — STRIPS: preconditions, effects, world-state search (Fikes & Nilsson, SRI)"
  - "2005 — F.E.A.R.: typed actions + a planner made the era's best game AI (Orkin, GDC '06)"
  - "2026 — your `@Action`s, planned the same way"
- **Cue:** squad chatter was faked; the planning was real.

### M2.4 — The mechanism
- **Layout:** diagram over log panel.
- **Graphic:** backward-chaining diagram: start at a `PersonalSchedule` goal star on the RIGHT,
  arrows walking LEFTWARD through needed types/actions to `UserInput` — arrows = "needs".
  Directly beneath, the log panel shows the forward inferred-plan line; a thin vertical
  alignment guide shows the two are the same chain reversed.
- **Copy:**
  - "The planner searches backwards from the goal type."
  - "`assemble` needs `ResearchedSessions` → only `research` produces it → so `research` runs
    first"
  - Log caption: "Same chain, printed at runtime. Nothing in the code says this order."

### M2.5 — The proof: two edits, zero rewiring
- **Layout:** before/after twins (log diff).
- **Graphic:** two log panels with identical geometry; before: `…shortlist → assemble`; after:
  `…shortlist → research → assemble` with the `research` token rendered as a green insertion
  (diff styling: subtle `+` gutter). Nothing else differs between the panels.
- **Copy:**
  - "Insert a step into the middle by only adding types."
  - "Edit 1: add `researchSessions(CandidateSessions) → ResearchedSessions`"
  - "Edit 2: make `assembleSchedule` consume `ResearchedSessions`"
  - "You reorder nothing. The plan re-routes itself."

### M2.6 — Legible failure
- **Layout:** single behaviour card.
- **Graphic:** the `research` node emitting `null` (an empty dashed outline where its output
  should be); from the planner box, a calm curved "replan" arrow looping back — explicitly NOT a
  crash: include a small crossed-out explosion icon to negate the expectation.
- **Copy:**
  - "`return null` → the planner replans. It doesn't crash, and it doesn't improvise."
  - "Compare: hard-wired pipeline → NPE. Model-driven loop → the model makes something up."

### M2.7 — Why not let the LLM plan?
- **Layout:** claim + converging evidence.
- **Graphic:** reuse the M0.8 convergence composition: paper card ("Blocksworld: ~12% solved
  out-of-the-box") + essay card (Rod Johnson) arrowing onto the judgement-inside/flow-outside
  architecture miniature.
- **Copy:**
  - "LLMs propose; planners dispose."
  - "GPT-4 solved ~12% of Blocksworld planning out of the box (Valmeekam et al., NeurIPS 2023)"
  - "The fix: an external sound planner — 'LLM-modulo' (ICML 2024). Embabel is that shape,
    productionised."
  - Caption: "Johnson, 'AI for your Gen AI' · 'Building Reliable Agentic Systems, Pt I'"
- **Cue:** be honest about the trade vs ReAct-style flexibility.

### M2.8 — GOAP is one of four
- **Layout:** four-up card row on a shared base.
- **Graphic:** four planner cards standing on ONE shared foundation slab labelled "the same
  typed actions & conditions": GOAP (lit blue, "you are here" tag), Utility AI, Supervisor/LLM,
  `@State` machines (all three dimmed). Each card carries a 3-word mini-glyph: fixed path /
  value dial / LLM-with-guard / state diagram.
- **Copy:**
  - "At 0.5.0, Embabel has four planner styles. Today: the most legible one."
  - "GOAP — fixed goal, derived path · Utility — no fixed goal, maximise value · Supervisor —
    LLM picks tools, types validate · `@State` — explicit machine, human-in-the-loop"
  - "The discipline you're building is what all four plan over."
- **Cue:** current differentiator vs LangGraph.

### M2.9 — Foil + lab
- **Layout:** forecast checklist + task card.
- **Graphic:** betting-slip card (one box) above a task card with branch chip
  `lab2-before → lab2-after`.
- **Copy:**
  - "☐ Calls research from INSIDE assemble — hard-wiring the flow the planner owns"
  - "Lab: add 2 records + 1 action → re-point `assembleSchedule` → read the plan diff → then
    `return null` and watch it replan"
  - "Acceptance: log reads `…shortlist → research → assemble` with no flow rewiring; you can
    explain the order from the types alone"
  - "Worksheet: `labs/lab2-goap.md` · 40 min"

### M2.10 — Keystone + transition
- **Layout:** keystone table, row 2 highlighted; ritual footer.
- **Copy:**
  - Row 2 lit.
  - "The planner routes through research now. It will also hand you a double-booked schedule
    with a straight face. Nothing yet says it CAN'T."

---

## Deck M3 — Guardrails: make the contract explicit (11 slides)
*Habit badge: "Habit 3 · Make the contract explicit". Slide M3.5 is the most important slide of
the day — give it maximum visual weight.*

### M3.1 — Module title
- **Layout:** section title.
- **Graphic:** plan ribbon; between `assemble` and a NEW `confirm` node, a padlock/gate motif
  mid-formation.
- **Copy:**
  - "M3 — Guardrails: preconditions & invariants"
  - "Delete the word 'hope' from the sentence."

### M3.2 — A request is not a guarantee
- **Layout:** quote-and-consequence.
- **Graphic:** foreground: a prompt card quoting the REAL line — "Hard rule: never pick two
  sessions in the same slot"; background: a long faded stack of identical schedule-run cards
  receding into depth, one of them red (the clash that got through). Depth = many runs over time.
- **Copy:**
  - "The prompt already says 'never double-book.' A prompt is a request."
  - "Honoured 99% of the time × thousands of runs = a steady stream of broken schedules."
- **Cue:** callback to 0.95¹⁰.

### M3.3 — Four shapes of guardrail
- **Layout:** four-row table with icon column.
- **Graphic:** typeset table; icon per row: shield (invariant), gate (precondition), meter
  (budget), key (access). Third column "Reassessed…" in emphasis colour.
- **Copy:**
  - "Invariant `noDoubleBooking` · `@Condition` + a goal that REQUIRES it · every planning cycle"
  - "Precondition `hasCandidates` · `@Condition` as `pre` · before the action runs"
  - "Budget · `Budget(0.50, 20, 200_000)` via `ProcessOptions` · continuously"
  - "Access control · `@SecureAgentTool` SpEL authority · before the tool runs"
- **Cue:** 'reassessed-when' is what makes these guardrails, not assertions.

### M3.4 — …and a fifth
- **Layout:** table addition.
- **Graphic:** a fifth row physically *sliding in* beneath the M3.3 table (motion implied by
  offset + soft trail), tinted differently, icon: filter/sieve.
- **Copy:**
  - "Content guardrail · `UserInputGuardRail` / `AssistantMessageGuardRail` · on every LLM
    exchange"
  - "Shapes 1–4 guard the PLAN. This one guards the CONTENT — deterministically, before any
    spend."
  - "Worked example on `main`: `RequestContentGuardRail` rejects prompt-injection markers."
- **Cue:** going-further in the lab; test proves it with no keys.

### M3.5 — THE TRAP (maximum visual weight)
- **Layout:** full-slide code card + verdict stamp.
- **Graphic:** the wrong code, set LARGE and syntax-highlighted, centre-stage:
  `@AchievesGoal @Action(post = {"noDoubleBooking"}) PersonalSchedule assembleSchedule(...)`.
  Across it, a rotated red rubber-stamp: "LOOKS LIKE IT ENFORCES. IT DOES NOT." Nothing else on
  the slide competes.
- **Copy:**
  - Stamp text as above.
  - "A `post` is a PLANNING PROMISE — the goal is 'achieved' the moment the output type exists,
    whether or not the post holds."
- **Cue:** not a bug — `post` exists so the planner can chain; gating is a different question.

### M3.6 — The fix: make the goal REQUIRE it
- **Layout:** three-step vertical mechanism diagram.
- **Graphic:** top: `assembleSchedule` box (tags: `post=noDoubleBooking`, `canRerun`) producing
  a `DraftSchedule` card → middle: condition DIAMOND `noDoubleBooking` with two exits — green
  edge down to `confirmSchedule` (goal star, tag `pre=noDoubleBooking`), red edge looping LEFT
  and back up to assemble; the red loop passes through a budget METER that visibly cuts it after
  N turns. Arrows = causality. This one diagram is the whole lesson; label every element.
- **Copy:**
  - "Split assemble → confirm. The invariant becomes a precondition the goal depends on."
  - "Clashing draft → condition FALSE → confirm can't run → planner re-runs assembly → budget
    stops it VISIBLY"
  - Mantra bar: "To make an invariant bite, the goal must require it as a `pre` — not promise
    it as a `post`."
- **Cue:** mirrors Embabel's own Researcher example.

### M3.7 — Design by Contract, one altitude up
- **Layout:** lineage bridge.
- **Graphic:** Meyer book-spine card + a chip typesetting the Hoare triple `{P} C {Q}`, arrows
  into a code chip of `pre`/`post`/`@Condition` annotations.
- **Copy:**
  - "Preconditions, postconditions, invariants — you've met these. Now they're plan-space."
  - "The twist: here `post` informs the planner; only `pre` on the goal is a wall."
  - Caption: "Meyer, 'Applying Design by Contract', IEEE Computer 1992 · Hoare 1969"

### M3.8 — Budget: the floor under everything
- **Layout:** single mechanism card.
- **Graphic:** one budget meter with three gauges ($0.50 · 20 actions · 200k tokens); behind it
  a retry spiral drawn tightening toward infinity but CUT by the meter's boundary line.
- **Copy:**
  - "The budget turns 'retry until it works' into 'retry, bounded, then stop visibly.'"
  - "`ProcessOptions.DEFAULT.withBudget(new Budget(0.50, 20, 200_000))`"
  - "The agent-era circuit breaker — the stop is a log line, not a mystery."
  - Caption: "Nygard, *Release It!* 2007"

### M3.9 — Deny before you spend
- **Layout:** single mechanism card.
- **Graphic:** the plan ribbon's main path running clean; hanging OFF the path, a
  `premiumBriefing` node behind a key-gate; a caller chip without the key bouncing off BEFORE
  reaching any LLM chip (place the LLM chip visibly downstream of the gate).
- **Copy:**
  - "`@SecureAgentTool(\"hasAuthority('conf:premium')\")` — refused before any LLM spend."
  - "Design note: `PremiumBriefing` is a side type the goal never consumes — access control
    without touching the free flow."

### M3.10 — Foil + lab
- **Layout:** forecast checklist + task card.
- **Copy:**
  - "☐ Strengthens the prompt ('IMPORTANT: never…') — hope, capitalised"
  - "☐ Adds a silent `if` that hides the violation instead of surfacing it"
  - "Lab: add `DraftSchedule` → two `@Condition`s → split assemble/confirm → budget → secured
    tool"
  - "Acceptance: a clashing draft NEVER reaches the goal (`GuardrailEnforcementTest`) · empty
    shortlist fails cleanly · budget stop visible in the log"
  - "Worksheet: `labs/lab3-guardrails.md` · 35 min"

### M3.11 — Keystone + transition (to lunch)
- **Layout:** keystone table, row 3 highlighted; ritual footer.
- **Copy:**
  - Row 3 lit.
  - "You've built an agent that would rather stop than lie. After lunch: what stopping looks
    like — and how to read WHY from the log instead of guessing."

---

## Deck M4 — Explainability: debug by reading state (11 slides)
*Habit badge: "Habit 4 · Read the plan, not the vibes". Post-lunch: keep slides dark,
theatrical, whodunit-flavoured.*

### M4.1 — Module title
- **Layout:** section title, ominous.
- **Graphic:** plan ribbon with the `confirm` node dark/dead and a red `STUCK` tag stamped over
  the chain; a faint magnifying-glass motif.
- **Copy:**
  - "M4 — Explainability: debug by reading state"
  - "A stuck agent is a LEGIBLE failure."

### M4.2 — The anti-pattern: prompt-poking
- **Layout:** loop diagram.
- **Graphic:** a circular despair-loop of four stations: edit prompt → re-run → squint at output
  → edit prompt…, drawn with increasingly frantic arrows; a clock spinning at the hub. Arrows =
  time wasted.
- **Copy:**
  - "When an LLM app misbehaves, the reflex is statistical debugging of a system you can't see."
  - "The 'fix' that finally works is a coincidence you can't explain in review."

### M4.3 — Two artefacts, two questions
- **Layout:** split panel with a named gap.
- **Graphic:** two tall panels: left "PLANNING LOG — what the planner believed and chose"
  (lighter blue), right "TRACE — what actually executed: spans, tokens, time" (deeper blue);
  between them a narrow dark seam explicitly labelled "where agent bugs live".
- **Copy:**
  - "Did the planner choose the wrong path — or did a chosen action behave wrongly?"
  - "Keeping the two views separate is what makes that question askable."

### M4.4 — The fault taxonomy
- **Layout:** diagnosis table, four rows.
- **Graphic:** each row pairs a MINI log-panel (the actual signature, styled) with its diagnosis:
  row 1 a `FALSE` token recurring; row 2 a type name absent/hollow; row 3 the
  `MaxActionsEarlyTerminationPolicy` line; row 4 a retry marker. Signatures must look visually
  distinct at a glance.
- **Copy:**
  - "Condition stays `FALSE` → a guardrail is refusing (today's fault)"
  - "Type never produced → a severed produces/consumes link (Lab 2's `null` shape)"
  - "Budget stop → the plan is spinning"
  - "Retriable vs non-retriable exception (0.4.0+) → transient fault, its own signature"
  - Footer: "Four faults, four log shapes. That's a diagnosis table, not vibes."

### M4.5 — The rule of the lab
- **Layout:** one huge fill-in-the-blanks sentence.
- **Graphic:** the template sentence set enormous with blank slots rendered as underlined gaps.
- **Copy:**
  - "Root cause in ONE sentence — before you touch the code."
  - "'[action] produces [state], so [condition] is always false and [goal action] can never
    run.'"

### M4.6 — The whodunit (demo interstitial)
- **Layout:** near-empty demo slide.
- **Graphic:** live log panel placeholder + magnifying glass.
- **Copy:**
  - "`git checkout lab4-broken` · `x \"…\" -p -r`  (keyless: `SPRING_PROFILES_ACTIVE=mock … ` → `plan \"…\"`)"
  - "Find the condition that never flips TRUE. Call it out."
- **Cue:** scroll WITH the room; consider `personality: severance` for this run. Keyless attendees
  run under the mock profile with `plan "…"` (the mock reproduces the stall via
  `MockModeIntegrationTest`; `x` fails keyless and `-p -r` are `x`-only, but `plan` prints the same
  stuck plan). No shell? `./mvnw test` already prints the world-state.

### M4.7 — The reveal
- **Layout:** one-line diff card.
- **Graphic:** a two-line diff, huge: red line `new ScheduleItem(s, "TBD")` with a slate chip
  beside it reading "compiles fine"; green line `new ScheduleItem(s, s.slot())`. Standard diff
  gutter (−/+). Nothing else.
- **Copy:**
  - "Every item pinned to slot \"TBD\" — any two sessions always clash."
  - "It compiles. It fails in the gap between the type system (satisfied) and the condition
    system (never satisfiable). Only the log sees that gap."

### M4.8 — Your trace is standard-issue
- **Layout:** lineage + compliance pair.
- **Graphic:** left: a timeline of chips Dapper 2010 → Zipkin 2012 → OpenTelemetry 2019 → GenAI
  semantic conventions (now); right: a small regulation card "EU AI Act Art. 12 — automatic
  event logging" with a checkmark pointing back at the log panel.
- **Copy:**
  - "Agent traces flow into the SAME observability stack as the rest of your estate."
  - "'confirm blocked because noDoubleBooking false' is an explanation a regulator — or an
    incident review — can read."
  - Caption: "Sigelman et al. 2010 · OTel GenAI semconv · EU AI Act Art. 12/86 · NIST AI RMF"
- **Cue:** cash in the M0 compliance promise.

### M4.9 — Observability you build (the black-box recorder)
- **Layout:** code + log pair with an event stream between them.
- **Graphic:** three-part composition, left to right. (1) A compact code card:
  `@Component class PlanFlightRecorder implements AgenticEventListener` with
  `onProcessEvent(...)` visible and ONLY the `@Component` annotation highlighted — the entire
  registration story is that one token. (2) An event stream: small labelled chips flowing from
  a platform box into the listener — "plan formulated" · "action executed (+duration)" ·
  "goal achieved" · "STUCK" (the STUCK chip red). Arrows = event delivery. (3) A log panel
  showing the real output line:
  `[flight-recorder] sharp_payne finished — 6 planning cycle(s), 5 replan(s), 6 action run(s)`.
- **Copy:**
  - "The third surface: any Spring bean implementing `AgenticEventListener` gets every event."
  - "The same stream the logging personalities render — plans, actions with durations, goals,
    `STUCK`"
  - "`PlanFlightRecorder` (on `main`): one summary line per run. On `lab4-broken`, the replan
    count IS the diagnosis — one integer."
  - "No Docker, no keys — it fires even inside the mocked integration tests. Pipe it into the
    observability estate you already run."
- **Cue:** observability stops being something you read and becomes something you build; the
  worksheet's going-further extends it to count LLM requests per action.

### M4.10 — Foil + lab
- **Layout:** forecast checklist + task card.
- **Copy:**
  - "☐ Pastes the stuck log into an agent and lets it guess — maybe even right, but
    unfalsifiable until YOU read the world-state. Evidence vs vibe."
  - "Lab: reproduce → read the log → (optional) Zipkin via Docker → state the sentence → fix one
    line → prove with the regression test"
  - "Acceptance: you name the failed condition BEFORE editing; goal span completes after"
  - "Worksheet: `labs/lab4-explainability.md` · 40 min"

### M4.11 — Keystone + transition
- **Layout:** keystone table, row 4 highlighted; ritual footer.
- **Copy:**
  - Row 4 lit.
  - "You can read the agent's mind. Now make it do MORE — without breaking what it already
    does."

---

## Deck M5 — Extend without breaking (10 slides)
*Habit badge: "Habit 6 · Extend by adding, not rewiring". Capstone lab follows.*

### M5.1 — Module title
- **Layout:** section title.
- **Graphic:** the plan ribbon DUPLICATING: a second parallel ribbon fading in beneath the
  first, sharing its first four node-shapes but ending in a new `planNetworking` node.
- **Copy:**
  - "M5 — Extend without breaking"
  - "Good architecture is measured at extension time."

### M5.2 — The test of the seams
- **Layout:** provocation slide.
- **Graphic:** the schedule pipeline with `assembleSchedule`, `confirmSchedule` and the
  guardrail diamonds wrapped in yellow-black "DO NOT TOUCH" barrier tape.
- **Copy:**
  - "Add a networking plan. Touch nothing the schedule depends on."
  - "If the feature forces you inside that tape, the seams were in the wrong place."

### M5.3 — Two constraints shape the design
- **Layout:** two framework-truth cards.
- **Graphic:** card 1: an agent box containing exactly ONE goal star, a second star bouncing
  off its wall; card 2: an agent's plan space drawn as a fence enclosing only its own action
  tiles, with a neighbouring agent's tiles visibly outside the fence. Both cards stamped small:
  "found by reading the planning log".
- **Copy:**
  - "1 — An `@Agent` supports ONE goal type. `PersonalSchedule` is taken → `NetworkingPlan`
    needs its own agent."
  - "2 — An agent plans ONLY with its own actions. No borrowing; `@EmbabelComponent` actions
    are NOT pooled."
  - "So the new agent must declare its own upstream steps. Copy-paste? No —"

### M5.4 — Share logic, not goals (centrepiece)
- **Layout:** full-slide architecture diagram.
- **Graphic:** centre: an amber `@Service ConfPlanningCapabilities` box (the ONLY amber element
  — the shared, boring, dependable thing); above and below, two blue agent boxes
  (`ConfPlannerAgent`, `ConfNetworkingAgent`), each containing a row of thin action tiles
  explicitly labelled "one-liners", every tile arrowing into the service. Arrows = delegation.
  Symmetry above/below.
- **Copy:**
  - "Pipeline LOGIC in a plain `@Service`; each agent declares thin wrappers."
  - "Shared logic, per-agent actions. Spring's oldest trick is the extension seam for agents
    too."

### M5.5 — Why one-goal-per-agent is a feature
- **Layout:** consequence comparison.
- **Graphic:** left: two SHORT, readable log panels side by side (one per agent), calm; right:
  one giant log panel overflowing the frame, crossed out. Same panel component, different scale.
- **Copy:**
  - "Every action widens the plan space and lengthens the trace you must read."
  - "Small agents keep the M4 habit possible. The God agent is the LLM-era God object."

### M5.6 — This principle has a name
- **Layout:** lineage bridge.
- **Graphic:** Parnas-paper card (1972) + OCP chip ("open for extension, closed for
  modification") arrowing into a miniature of the M5.4 diagram.
- **Copy:**
  - "Open for extension, closed for modification — enforced by a planner, not a code review."
  - "You CAN'T tweak another agent's flow: plans only route through an agent's own typed
    actions. The principle became a physical property."
  - Caption: "Parnas 1972 · Meyer/OCP 1988"

### M5.7 — The proof is the regression
- **Layout:** evidence slide.
- **Graphic:** a test-suite panel listing the schedule integration test +
  `GuardrailEnforcementTest`, each with a green tick, repeated across a three-phase timeline
  strip "before / during / after the change" — ticks identical in all three phases.
- **Copy:**
  - "'Provably untouched' is a TEST claim, not a diff claim."
  - "The only schedule-path edit moves prompt bodies into the service — same plan, same output,
    green the whole time."

### M5.8 — Foil + lab
- **Layout:** forecast checklist + task card.
- **Copy:**
  - "☐ Bolts networking onto the schedule goal ☐ Edits `assembleSchedule` to also emit people
    ☐ Copy-pastes the pipeline into a second class"
  - "Lab: add `NetworkingPlan` → extract the `@Service` → make `ConfPlannerAgent` delegate →
    add `ConfNetworkingAgent` with its own wrappers + `planNetworking` goal"
  - "Acceptance: networking log shows the reused pipeline; schedule flow and ALL its tests
    unchanged"
  - "Worksheet: `labs/lab5-extend.md` · 45 min · Track C preferred"

### M5.9 — If ConfPlanner grew (planner choice)
- **Layout:** three mini-scenario cards.
- **Graphic:** three cards, each pairing a scenario icon with the planner glyphs from M2.8:
  evolving-preferences calendar → Utility dial; wizard steps with a human icon → `@State`
  diagram; open chat bubble → Supervisor glyph. Beneath all three, the same shared foundation
  slab from M2.8.
- **Copy:**
  - "Extension also means choosing a planner on purpose."
  - "Preferences evolving across a multi-day conference → Utility · Registration wizard with
    human confirmation → `@State` · Open-ended concierge → Supervisor"
  - "Same typed actions underneath all of them."

### M5.10 — Keystone + transition
- **Layout:** keystone table, row 5 highlighted; ritual footer.
- **Copy:**
  - Row 5 lit.
  - "Two agents, five LLM calls per run — some of them pulling five fields out of a sentence
    with your most expensive model. Last habit: right-size it."

---

## Deck M6 — Model routing: the cheapest model that passes (11 slides)
*Habit badge: "Habit 5 · Right-size the model".*

### M6.1 — Module title
- **Layout:** section title.
- **Graphic:** plan ribbon re-rendered with nodes SIZED by spend — `assemble` visibly the
  largest/heaviest node, `confirm` a hairline. Size = cost.
- **Copy:**
  - "M6 — Model routing: the cheapest model that passes"
  - "Cost is a design parameter, not an afterthought."

### M6.2 — The window is the budget (reframe — ties four labs together)
- **Layout:** reframe slide; a central "window" frame with four inward-pressing levers.
- **Graphic:** one rectangle labelled "the context window — one finite budget every call spends
  against". Four inward arrows, each tagged with the lab that taught it: DICE (Lab 1) "say the rule
  ONCE" · Budget guardrail (Lab 3) "cap the spend" · Routing (Lab 6, lit) "price each step" · RAG
  (Lab 7, dashed = take-home) "shrink the menu: 500 sessions → the relevant handful".
- **Copy:**
  - "Everything you've done all day was managing ONE resource: the context window."
  - "The window is a budget, not a container. Tokens in + tokens out + the model's ceiling all
    compete for it."
  - "DICE fills it efficiently · guardrails cap it · routing prices it · RAG shrinks it."
- **Cue:** the synthesis beat — the room has lived three of the four levers, so name the through-line
  before teaching the fourth. "Context engineering" (M0) IS context-window management. Cross-turn
  *memory* is a different window problem — flag it as the M7 horizon, don't open it here.

### M6.3 — The uneven-spend picture
- **Layout:** one annotated stacked bar.
- **Graphic:** a single horizontal 100% bar segmented: extract+shortlist ~15% · research ~35% ·
  assemble ~45% · confirm ~0% (drawn as a hairline sliver, callout "plain code"). Segment labels
  inside; percentages large.
- **Copy:**
  - "Where one run actually spends."
  - "If everything uses your best model, you're paying premium rates to pull five fields out of
    a sentence."

### M6.4 — The opposite of right-sizing isn't 'cheap'
- **Layout:** statement slide.
- **Graphic:** one large greyed default-model chip with all five action tiles funnelling into it
  indiscriminately — a visual of *no decision being made* (uniform grey, no routing).
- **Copy:**
  - "The opposite of right-sizing is NOT DECIDING."
  - "One default absorbing work it's over-qualified for. Right-sizing makes each step's cost a
    decision you can defend — or reduce."

### M6.5 — The heuristic (poster)
- **Layout:** rule poster + application table.
- **Graphic:** the rule set large as a two-branch decision: return-type card "flat list of
  strings" → small `cheapest` chip; return-type card "scores · rationale · conflict-free
  arrangement" → large `best` chip. Below, the five-action table with role chips coloured.
- **Copy:**
  - "Flat list of strings → `cheapest`. Carries judgement → `best`."
  - "extract → cheapest · shortlist → cheapest · research → best · assemble → best · confirm →
    (no model — plain code)"
  - "The return type is an honest proxy for how much judgement the step demands."
- **Cue:** the id-only idiom (M1) keeps cheap steps cheap.

### M6.6 — Config, not code
- **Layout:** code + config twins.
- **Graphic:** left card: Java with exactly ONE highlighted token — `withLlmByRole("cheapest")`;
  right card: the yml role map. A rotating-plug icon between them: the binding is the moving
  part, the code is not.
- **Copy:**
  - "Code commits to a ROLE. Config binds the role to whatever is current."
  - "`embabel.models.llms: { cheapest: gpt-4.1-nano, best: gpt-4.1 }`"
  - "Model names churn quarterly; roles don't. Re-routing the estate is a config edit — and the
    tests mock the LLM, so it builds green with no keys."

### M6.7 — The industry built this layer too
- **Layout:** evidence row + contrast.
- **Graphic:** three small evidence cards (FrugalGPT · RouteLLM · provider auto-routers) above;
  below, Embabel's role-map table rendered as a clean, readable, human-signed document —
  contrast "learned black box" (cards show tiny opaque network glyphs) vs "reviewable table".
- **Copy:**
  - "Per-request model choice is now a first-class layer everywhere."
  - "FrugalGPT: GPT-4-level quality at up to ~98% lower cost via cascades (Chen, Zaharia, Zou
    2023) · RouteLLM (LMSYS 2024)"
  - "Embabel's version is a human-reviewable table, not a learned black box — in a regulated
    shop, that's a feature."

### M6.8 — The residency lever
- **Layout:** split territory diagram.
- **Graphic:** left territory: a building outline (amber border) containing a local-model chip
  with the `extract` node routed into it; right territory: a cloud outline containing the strong
  model with `research`/`assemble` routed in. Between them, the yml role map drawn as a physical
  SWITCH — the only element that moves. A small scale icon labels the trade: "latency + some
  quality ⇄ residency".
- **Copy:**
  - "Same knob, different question: what if this step's data can't leave the building?"
  - "Bind `cheapest` to an Ollama tag under a Spring profile. Code unchanged. Trade named — and
    RECORDED in `MODEL_ROUTING.md`."

### M6.9 — Measure before optimising
- **Layout:** comparison twins + third-lever chip.
- **Graphic:** two log panels, identical geometry: "all-`best` run" vs "routed run", per-call
  COST lines highlighted; a delta callout between them showing the saving. Beneath, a small
  chip-row of the three levers: routing dial · budget meter · cache icon.
- **Copy:**
  - "Since 0.4.0 the log prices each call. Read the comparison in dollars per action."
  - "A routing decision you can't see in the log is a guess. Record it in `MODEL_ROUTING.md`'s
    observed-cost table."
  - "Routing picks the model · the budget caps the run · prompt caching (0.4.0+) cheapens the
    repeats."

### M6.10 — Foil + lab
- **Layout:** forecast checklist + task card.
- **Copy:**
  - "☐ Hard-codes a cheaper model name in Java — wrong by next quarter ☐ Downgrades
    EVERYTHING, including assemble"
  - "Lab: route extract/shortlist → `cheapest`, research/assemble → `best` → confirm the yml
    map → update `MODEL_ROUTING.md` → justify every row by return type"
  - "Acceptance: only `withLlmByRole(...)` calls changed; the routing table matches the code
    and you can defend each row"
  - "Worksheet: `labs/lab6-model-routing.md` · 30 min · Track C"

### M6.11 — Keystone + transition
- **Layout:** keystone table, row 6 highlighted — table now FULLY lit for the first time;
  ritual footer.
- **Copy:**
  - Row 6 lit; all rows now bright.
  - "Six habits, six mechanisms — and two habits that ran through everything. Let's close the
    loop on the loop."

---

## Deck M7 — Wrap: govern the loop (10 slides)
*No habit badge (habits 7–8 are introduced here). Closing energy: consolidating, generous,
send-them-home-tall.*

### M7.1 — Module title
- **Layout:** section title.
- **Graphic:** the blue plan ribbon and the amber harness drawn, for the first time, as ONE
  closed loop — the ribbon curling round and the amber ring enclosing it.
- **Copy:**
  - "M7 — Govern the loop"
  - "Two habits ran through everything. Now the loop closes."

### M7.2 — The full stack
- **Layout:** eight-row table (first full reveal).
- **Graphic:** the HABITS.md table: rows 1–6 each carrying its module's accent; rows 7–8 set
  apart, spanning full width with a subtle "ran all day" underlay.
- **Copy:** the eight-habit table verbatim, plus:
  - "Rows 1–6: one lab each. Rows 7–8: every lab."

### M7.3 — Habit 7: test the seams
- **Layout:** evidence slide.
- **Graphic:** the action boundary drawn as a literal SEAM (stitched line) through a component;
  left of the seam a mock chip (`FakeOperationContext` · `whenCreateObject`), right of it a real
  model chip; on the test-suite card, a bold badge: "0 API keys".
- **Copy:**
  - "You built and tested an LLM agent all day with NO keys."
  - "The seam is the action boundary — mock the model, assert on plan completion and
    invariants."
  - "Test the deterministic shell, not the prose: `GuardrailEnforcementTest` asserts a REFUSAL."
  - Caption: "Feathers, *Working Effectively with Legacy Code* 2004 — the seam, verbatim"

### M7.4 — Habit 8: govern the loop
- **Layout:** cycle diagram.
- **Graphic:** a six-station closed loop: model → goal → guardrail → read → right-size → extend,
  each station carrying its module's icon from earlier decks (record card, goal star, gate
  diamond, magnifying glass, routing dial, twin ribbons); at the hub, the ritual's three icons.
  Arrows = the cycle, clockwise.
- **Copy:**
  - "The whole cycle, made muscle memory."
  - Hub: "Read the planning log · Read the trace · Confirm the acceptance check"
  - "A checklist in the Gawande sense — externalised discipline, not memory."

### M7.5 — The ladders, walked
- **Layout:** the M0.10 twin ladders, revisited.
- **Graphic:** the two ladders from M0.10, identical geometry. On the OPERATIONAL ladder, lab
  chips PLACED where each lab sat (Lab 1 at Dictating/Commanding, Lab 6 at Orchestrating) with
  a traced path arrow climbing through them — the day, plotted. On the COGNITIVE ladder, no
  path: a self-assessment prompt card hangs off it instead. The Build Gap caliper from M0.10
  now reads "yours to measure".
- **Copy:**
  - "The operational ladder — walked. The cognitive ladder — yours."
  - "You spent today climbing from Dictating to Orchestrating. That was the habitat's ladder."
  - "The take-home: score your three disciplines — context engineering, architectural
    constraints, guardrail design. The weakest one is your ceiling."
  - "The top rungs aren't more automation. They're more judgement per keystroke."
- **Cue:** the scoring heuristic is the cognitive ladder's: the assessed level is the highest
  with substantial evidence across ALL three disciplines — strong specs with weak verification
  is L2, not L4.

### M7.6 — The punchline
- **Layout:** the mirror, resolved (THE closing visual — invest here).
- **Graphic:** the M0.9 two-column mirror animating/resolving into two NESTED loops: the amber
  build-time loop wrapping the blue runtime loop, the same three discipline labels appearing
  once on each loop at matching positions. Recursion depicted as containment.
- **Copy:**
  - "Track C built the Embabel agent using the very disciplines the agent embodies."
  - "The orchestrator named a goal and let a planning loop derive the steps — to build an agent
    that names a goal and lets a planning loop derive the steps."
  - "The harness is a planning loop for your engineering."
  - "The two crafts from this morning — agentic building and building agents — were one loop
    all along."

### M7.7 — Monday morning
- **Layout:** three action cards.
- **Graphic:** three equal cards, each led by its mechanism icon (gate diamond / log panel /
  routing table) — deliberately plain, do-able, un-glossy.
- **Copy:**
  - "Three moves, this week, in YOUR codebase:"
  - "1 — Pick ONE invariant your system must never violate. Make it a condition your goal
    REQUIRES, with a test that proves it bites."
  - "2 — Add ONE trace surface: log what your orchestration BELIEVED, not just what it called."
  - "3 — Write ONE routing table: which model serves which step, justified by return type. One
    row is enough to start."

### M7.8 — Where this is going
- **Layout:** horizon slide.
- **Graphic:** a road receding to the horizon with three markers: "0.5.0 'Darwin'" (large, lit,
  flagged YOU ARE HERE) → "1.0 RC" (near, sharp) → "2.0" (far, hazy). Roadside signs carry the
  bullet items.
- **Copy:**
  - "You learned on 0.5.0 — the current release. Here's the road."
  - "1.0.0-RC1 in flight: deprecated APIs already purged on `main` — re-verify before you
    re-pin"
  - "2.0 line: Spring Boot 4 · Spring AI 2.0 · Jackson 3"
  - "Beyond GOAP: Utility, Supervisor, `@State` planners · agentic RAG (`ToolishRag`) · content
    guardrails · per-call cost tracking — start with `labs/lab7-rag.md`"
  - "Beyond one turn: cross-turn memory — `embabel-chat-store` persists a session, `@State` carries
    world-state across turns. ConfPlanner is deliberately single-turn; memory is the next shape"
- **Cue:** the repo's risk register tracks this. Two distinct horizons: RAG shrinks the within-a-call
  window (M6 reframe); memory extends the agent across calls. Both "where next," neither in scope today.

### M7.9 — Where to go next
- **Layout:** resource grid, six cards.
- **Graphic:** six link cards, each icon + one line; QR codes if venue suits.
- **Copy:**
  - "This repo's docs site — tutorials → how-to → reference → explanation (and the risk
    register)"
  - "Embabel Guide — an MCP server that answers Embabel questions INSIDE Claude Code (SETUP §7)"
  - "Embabel Hub — hub.embabel.com · talk to the docs"
  - "`embabel/coding-agent` — what big looks like, dogfooded by the Embabel team"
  - "`embabel-agent-examples` — tracks snapshots: current best practice"
  - "Rod Johnson's essays — 'AI for your Gen AI' · 'Building Reliable Agentic Systems'"

### M7.10 — Close
- **Layout:** final slide.
- **Graphic:** two lines of type above the keystone table rendered small, complete, fully lit —
  earned; the ritual footer beneath.
- **Copy:**
  - "'The sovereign engineer is not the one who ignores AI, nor the one who surrenders to
    it.'"
  - "Read the plan. Read the trace. Confirm the check."
  - "Thank you — go make failure legible."
  - Caption: "Miles, *The Sovereign Engineer*"
- **Cue:** the full quote if asked: "…the one who understands the architecture of the
  collaboration — amplifier and reference frame, power and direction, capability and
  judgment — and invests accordingly."
