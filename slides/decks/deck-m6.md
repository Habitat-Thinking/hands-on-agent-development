# Deck M6 — Model routing: the cheapest model that passes (11 slides)

> Shown before Lab 6, the last lab. Habit badge: "Habit 5 — Right-size the model".
> Speaker notes: `../notes/m6-model-routing.md`.

## Slide 1 — Module title
- **Layout:** section title.
- **Visual:** plan ribbon with nodes now sized/coloured by spend — `assemble` node visibly the
  heaviest.
- **On-slide copy:**
  - "M6 — Model routing: the cheapest model that passes"
  - "Cost is a design parameter, not an afterthought."

## Slide 2 — The window is the budget (reframe — ties four labs together)
- **Layout:** reframe slide; one central "window" frame with four inward-pointing levers.
- **Visual:** a single rectangle labelled "the context window — one finite budget every call spends
  against". Four labelled arrows pressing on it, each tagged with the lab that taught it: DICE
  (Lab 1) "say the domain rule ONCE, not per prompt" · Budget guardrail (Lab 3) "cap the spend" ·
  Routing (Lab 6, lit) "price each step" · RAG (Lab 7, dashed/take-home) "shrink the menu — 500
  sessions → the relevant handful".
- **On-slide copy:**
  - Headline: "Everything you've done all day was managing ONE resource: the context window."
  - Sub: "The window is a budget, not a container. Tokens in + tokens out + the model's ceiling all
    compete for it — and each habit is a different lever on the same budget."
  - Lever line: "DICE fills it efficiently · guardrails cap it · routing prices it · RAG shrinks it."
- **Notes:** this is the synthesis beat — by now the room has *lived* three of the four levers, so
  name the through-line before teaching the fourth. "Context engineering" (the M0 discipline) IS
  context-window management; this slide cashes that in. Memory across turns is a *different*
  window problem — flag it as the M7 horizon, don't open it here.

## Slide 3 — The uneven-spend picture
- **Layout:** one horizontal stacked bar, annotated.
- **Visual:** token-share bar of one schedule run: extract+shortlist ~15% · research ~35% ·
  assemble ~45% · confirm ~0% (rendered as a hairline labeled "plain code").
- **On-slide copy:**
  - Headline: "Where one run actually spends."
  - Sub: "If everything uses your best model, you're paying premium rates to pull five fields out
    of a sentence."

## Slide 4 — The opposite of right-sizing isn't 'cheap'
- **Layout:** statement slide.
- **Visual:** a single default-model chip silently absorbing all five actions, greyed.
- **On-slide copy:**
  - Headline: "The opposite of right-sizing is NOT DECIDING."
  - Sub: "One default absorbing work it's over-qualified for. Right-sizing makes each step's cost
    a decision you can defend — or reduce."

## Slide 5 — The heuristic (poster slide)
- **Layout:** rule poster + application table.
- **Visual:** big rule at top; the five-action routing table beneath, roles as coloured chips.
- **On-slide copy:**
  - Rule: "Flat list of strings → `cheapest`. Carries judgement — scores, rationale, a
    conflict-free arrangement → `best`."
  - Table rows: "extract → cheapest · shortlist → cheapest · research → best · assemble → best ·
    confirm → (no model — plain code)"
  - Sub: "The return type is an honest proxy for how much judgement the step demands."
- **Notes:** id-only idiom (M1) is what keeps the cheap steps cheap.

## Slide 6 — Config, not code
- **Layout:** code + config pair.
- **Visual:** left: `ai.withLlmByRole("cheapest")` (the ONLY code change, highlighted); right:
  the `application.yml` role map.
- **On-slide copy:**
  - Headline: "Code commits to a ROLE. Config binds the role to whatever is current."
  - Config: `embabel.models.llms: { cheapest: gpt-4.1-nano, best: gpt-4.1 }`
  - Sub: "Model names churn quarterly; roles don't. Re-routing the estate is a config edit — and
    the tests mock the LLM, so it builds green with no keys."

## Slide 7 — The industry built this layer too
- **Layout:** evidence row.
- **Visual:** three small cards: FrugalGPT, RouteLLM, provider-side routers; Embabel's role map
  beneath as "the reviewable version".
- **On-slide copy:**
  - Headline: "Per-request model choice is now a first-class layer everywhere."
  - "FrugalGPT: GPT-4-level quality at up to ~98% lower cost via cascades (Chen, Zaharia, Zou
    2023) · RouteLLM (LMSYS 2024) · provider auto-routers"
  - Sub: "Embabel's version is a human-reviewable table, not a learned black box — in a regulated
    shop, that's a feature."

## Slide 8 — The residency lever
- **Layout:** split cloud/building diagram.
- **Visual:** extraction step routed to a local model inside a building outline (amber border);
  the rest of the pipeline in a cloud outline; the role map as the switch between them.
- **On-slide copy:**
  - Headline: "Same knob, different question: what if this step's data can't leave the building?"
  - Sub: "Bind `cheapest` to an Ollama tag under a Spring profile. Code unchanged. Trade named —
    latency and some quality for residency — and RECORDED in `MODEL_ROUTING.md`."
- **Notes:** hands-on recipe (key-free, `local` profile → Ollama) is
  `docs/how-to/route-a-step-to-a-local-model.md`; full notes in `../notes/m6-model-routing.md` beat 5.

## Slide 9 — Measure before optimising
- **Layout:** comparison card (NEW — 0.4.0 content).
- **Visual:** two log panels: all-`best` run vs routed run; per-call COST lines highlighted, a
  delta callout between them.
- **On-slide copy:**
  - Headline: "Since 0.4.0 the log prices each call. Read the comparison in dollars per action."
  - Sub: "A routing decision you can't see in the log is a guess. Record what you observe in
    `MODEL_ROUTING.md`'s observed-cost table."
  - Third lever chip: "…and prompt caching (0.4.0+) cuts the price of what's left: routing picks
    the model, the budget caps the run, caching cheapens the repeats."

## Slide 10 — Foil + lab
- **Layout:** checklist + task card.
- **On-slide copy:**
  - Forecast (Track B): "☐ Hard-codes a cheaper model name in Java — wrong by next quarter
    ☐ Downgrades EVERYTHING, including assemble"
  - Lab: "Route extract/shortlist → `cheapest`, research/assemble → `best` → confirm the yml
    map → update `MODEL_ROUTING.md` → justify every row by return type"
  - Acceptance: "only `withLlmByRole(...)` calls changed; the routing table matches the code and
    you can defend each row"
  - "Worksheet: `labs/lab6-model-routing.md` · 30 min · Track C"

## Slide 11 — Keystone + transition
- **Layout:** keystone table, row 6 highlighted — table now FULLY lit; ritual footer.
- **On-slide copy:**
  - Row 6 lit: "Right-size the model · `withLlmByRole` cheap-vs-strong · Right-size the model"
  - Transition: "Six habits, six mechanisms — and two habits that ran through everything.
    Let's close the loop on the loop."
