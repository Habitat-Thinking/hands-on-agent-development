# Deck M0 — Orientation (14 slides)

> Shown 09:00–09:40. Speaker notes: `../notes/m0-orientation.md`. Uses design system in
> [README.md](README.md). No habit badge on this deck (habits are introduced here).

## Slide 1 — Title
- **Layout:** full-bleed title.
- **Visual:** dark field; the plan ribbon (all six nodes, unlit) as a faint horizontal motif.
- **On-slide copy:**
  - "Hands-On AI Agent Engineering"
  - "Habits for safe, explainable, domain-grounded AI"
  - "ConfPlanner · a goal-oriented Embabel agent · Java 21"
- **Notes:** while attendees run `./mvnw clean verify` (W4 mitigation).

## Slide 2 — Cold open: whose fault is this?
- **Layout:** single artifact + one question.
- **Visual:** a conference schedule card with two sessions stacked in the same 10:00 slot,
  clash highlighted in red.
- **On-slide copy:**
  - Headline: "An agent built this."
  - Sub: "Whose fault is the double-booking — the model's, or the system that let the answer
    through?"
- **Notes:** don't answer; the day answers.

## Slide 3 — Why agents fail in production
- **Layout:** 2×2 grid, each cell tagged with the lab that fixes it.
- **Visual:** four cells, each a failure icon + lab chip.
- **On-slide copy:**
  - "Ungrounded domain — nothing typed constrains the model → Lab 1"
  - "Hidden steps — flow is hard-wired, nobody can say why step 3 runs → Lab 2"
  - "No contract — 'please don't' lives in a prompt → Lab 3"
  - "No trace — debugging = vibes and re-runs → Lab 4"
- **Notes:** each cell is a module; say so.

## Slide 4 — The arithmetic of hope (and the amplifier)
- **Layout:** one huge equation, one thesis line.
- **Visual:** `0.95¹⁰ ≈ 0.60` rendered enormous; a 10-step chain where each link fades slightly.
  Beneath, a small amplifier glyph with two inputs and two outputs: "discipline in → excellence
  out" / "mess in → wreckage out".
- **On-slide copy:**
  - Headline: "A 95%-reliable step, ten times in a row, is a 60%-reliable plan."
  - "AI does not replace engineering judgment. It amplifies whatever engineering judgment you
    already have." — the amplifier thesis
  - Sub: "DORA 2025: high performers improved across the board; low performers had their
    problems amplified. The variable was the practices, not the tool."
  - Citation line: "Miles, *The Sovereign Engineer* ch. 1 · DORA 2025"
- **Notes:** do the arithmetic out loud; the amplifier thesis is why structure (not model
  choice) is the day's subject.

## Slide 5 — Two crafts hide inside "AI agents"
- **Layout:** side-by-side twins with a verdict bar beneath.
- **Visual:** two panels with identical geometry. Left panel (amber): a human figure at the top
  of a small ladder supervising a crew of agent chips assembling a structure — agents as
  *workforce*. Right panel (blue): a single agent box on an engineering bench — typed ports,
  planner gear, gate diamond — the agent as *product*. Beneath both, one full-width verdict bar.
- **On-slide copy:**
  - Headline: "'AI agents' is two different crafts. Name which one you're doing."
  - Left: "AGENTIC BUILDING — agents as your workforce: agentic workflows, harnesses, habitat
    thinking"
  - Right: "BUILDING AGENTS — the agent as your product: typed actions, planning, guardrails
    (Embabel)"
  - Verdict bar: "Today is mostly BUILDING AGENTS — and you'll drive it VIA agentic building."
- **Notes:** most confusion in this space is these two crafts talked about as one; naming them
  is the day's first gift.

## Slide 6 — Habitat thinking, in one slide
- **Layout:** definition slide with one central diagram.
- **Visual:** a habitat boundary drawn around a human figure and agent chips working together —
  explicitly an *environment*, not a cage: the boundary built from FOUR labelled bricks, the
  book's own components — "conventions — accumulated wisdom, encoded", "architectural
  constraints — boundaries the AI cannot intuit", "test suites — the metacognition the AI
  lacks", "feedback loops — catch drift before it compounds" — with open gates through it.
  Outside, small and dismissed, a crossed-out speech bubble labelled "prompting harder".
- **On-slide copy:**
  - Headline: "Stop optimising the prompt. Design the habitat."
  - "'A habitat is a persistent, evolving collaboration environment.'"
  - "'An AI agent without a well-designed habitat is just model cognition with a toolbox.' The
    habitat provides the cognitive architecture the agent itself lacks."
  - Sub: "A habitat makes the desired behaviour the path of least resistance. A cage makes all
    behaviour difficult. You'll live in one all day: the Track C harness IS a habitat."
  - Citation line: "Miles, *The Sovereign Engineer* — habitat engineering, chs. 2–3"
- **Notes:** 60–90 seconds, not a book talk. The "model cognition with a toolbox" line is the
  bridge: it's true of your coding agents AND of ConfPlanner — which is why the day builds a
  runtime habitat (typed actions, conditions, budgets) around a model. The dual-harness slide
  (9) cashes this in.

## Slide 7 — What Embabel is
- **Layout:** split definition slide.
- **Visual:** left: LLM chip inside a typed action box (blue); right: planner box connecting
  actions. A "Spring lineage" footnote line.
- **On-slide copy:**
  - Headline: "Embabel: the LLM does the judgement; a deterministic planner does the flow."
  - "Typed `@Action`s — the model works inside a contract"
  - "GOAP planner — the order is derived, not written"
  - "By Rod Johnson (creator of Spring) · JVM-native · Spring DI, testing, security"
  - Citation line: "Johnson, 'Embabel: A new agent platform for the JVM' · The New Stack interview"
- **Notes:** the EJB→Spring rhyme for this audience.

## Slide 8 — Not just our opinion
- **Layout:** two quotes, one convergence line.
- **Visual:** academic paper card (left) + practitioner card (right) pointing at the same
  architecture diagram from slide 7.
- **On-slide copy:**
  - Headline: "Theory and practice agree: don't let the model be the planner."
  - "Kambhampati et al.: LLMs generate *plausible* plans, not *valid* ones — pair them with a
    sound planner ('LLM-modulo', ICML 2024)"
  - "Anthropic, *Building Effective Agents*: most production value is structured workflows, not
    free-running loops"
- **Notes:** 3 minutes here; it's the intellectual spine.

## Slide 9 — The dual harness
- **Layout:** two-column mirror table (THE motif slide).
- **Visual:** amber column left (build-time), blue column right (runtime); three discipline rows
  bridging both.
- **On-slide copy:**
  - Headline: "You will work inside two harnesses today. They teach the same three disciplines."
  - Rows: "Context engineering · Architectural constraints · Guardrail design"
  - Left header: "Build-time — the agentic-building craft, governing your engineering"
  - Right header: "Runtime — the building-agents craft: Embabel, governing the agent"
- **Notes:** the slide-5 distinction becomes architecture here; promise the punchline for 17:00.

## Slide 10 — Two ladders (and the gap between them)
- **Layout:** twin vertical ladders + a caliper.
- **Visual:** two parallel ladders side by side. Left, "OPERATIONAL — what your habitat
  delivers": five rungs, Dictating → Commanding → Regulating → Orchestrating → Supervising,
  with Track chips A/B pinned low and C spanning the top two. Right, "COGNITIVE — what you can
  think and do": six rungs, the book's personas — Aware → Prompter → Verifier → Habitat
  Engineer → Specification Architect → Sovereign Engineer. Between the ladders, a caliper
  measuring the vertical distance between two "you are here" markers, labelled "the Habitat
  Build Gap".
- **On-slide copy:**
  - Headline: "Two ladders: what you can think, and what your habitat delivers."
  - "Today's labs climb the OPERATIONAL rungs — by Lab 6 you're supervising, not typing."
  - "The COGNITIVE climb is the take-home — sovereignty is the top rung, not the entry fee."
  - Sub: "The gap between the two is the diagnostic. Coherence — not raw level — is the signal."
  - Citation line: "Agentic Experience 5-level maturity model (TechTalk) · the cognitive
    ladder, Miles, *The Sovereign Engineer* — both in the repo's `reference/`"
- **Notes:** don't teach the 14 dimensions; plant the two-reads idea and the Build Gap word.
  "Higher rung = more judgement per keystroke" still applies — to both ladders. The levels are
  an expanding repertoire, not a graduation: a Sovereign Engineer still writes prompts.

## Slide 11 — The keystone
- **Layout:** full-slide keystone table (recurring component #1), all rows lit.
- **Visual:** 6 rows: objective → Embabel mechanism → habit; lab chips on the right.
- **On-slide copy:** the README table verbatim (rows 1–6).
- **Notes:** "we return to this slide six times."

## Slide 12 — How the day works
- **Layout:** three horizontal bands.
- **Visual:** band 1: branch walk `lab1-before → lab1-after … lab4-broken → lab4-after …`;
  band 2: the three track chips with one-liners; band 3: the ritual footer.
- **On-slide copy:**
  - "Operate git branches: `-before` → worksheet → diff against `-after`"
  - "Track A by hand · Track B ungoverned agent (the foil) · Track C through the harness"
  - "`lab4-broken` is the one branch that fails `verify` — on purpose"
  - Ritual: "Read the planning log · Read the trace · Confirm the acceptance check"
- **Notes:** logistics fast; worksheets carry detail.

## Slide 13 — Live demo
- **Layout:** demo interstitial (near-empty).
- **Visual:** log panel component, empty, cursor blinking.
- **On-slide copy:**
  - "`x \"I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a
    schedule\" -p`"
  - Sub: "Nobody wrote the sequence you're about to see."
- **Notes:** demo script in notes; goal = make the planning log an object of curiosity.
  **Keyless venue?** run `SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run` and invoke with
  `plan "…"` instead — `x` ranks the request against goals with an LLM call, so it fails under the
  mock profile (`Text content cannot be empty`); `plan` invokes the goal directly and prints the
  planning log with no flags. See `docs/how-to/use-mock-mode.md`.

## Slide 14 — Transition
- **Layout:** single line.
- **Visual:** failure grid from slide 3 with cell 1 lit.
- **On-slide copy:**
  - "First failure mode: the ungrounded domain."
  - "`git checkout lab1-before`"
- **Notes:** straight into M1 frame.
