# Deck M4 — Explainability: debug by reading state (11 slides)

> Shown 13:50, first after lunch — run as a whodunit; keep slides dark and theatrical.
> Habit badge: "Habit 4 — Read the plan, not the vibes". Speaker notes:
> `../notes/m4-explainability.md`. Demo tip: run the broken branch with
> `embabel.agent.logging.personality: severance` (or `starwars`) — same evidence, wide-awake room.

## Slide 1 — Module title
- **Layout:** section title, ominous.
- **Visual:** plan ribbon with the `confirm` node dark and a red `STUCK` tag over the chain.
- **On-slide copy:**
  - "M4 — Explainability: debug by reading state"
  - "A stuck agent is a LEGIBLE failure."

## Slide 2 — The anti-pattern: prompt-poking
- **Layout:** loop diagram.
- **Visual:** a despair loop: edit prompt → re-run → squint → edit prompt…; a clock spinning.
- **On-slide copy:**
  - Headline: "When an LLM app misbehaves, the reflex is statistical debugging of a system you
    can't see."
  - Sub: "The 'fix' that finally works is a coincidence you can't explain in review."

## Slide 3 — Two artefacts, two questions
- **Layout:** split panel.
- **Visual:** left panel "PLANNING LOG — what the planner believed and chose" (blue); right panel
  "TRACE — what actually executed: spans, tokens, time" (blue, deeper). A gap between them
  labeled "where agent bugs live".
- **On-slide copy:**
  - Headline: "Did the planner choose the wrong path — or did a chosen action behave wrongly?"
  - Sub: "Keeping the two views separate is what makes that question askable."

## Slide 4 — The fault taxonomy
- **Layout:** three-row (plus one) diagnosis table (promoted from the worksheet).
- **Visual:** each row: log-signature snippet (mini log panel) + diagnosis.
- **On-slide copy:**
  - "Condition stays `FALSE` → a guardrail is refusing (today's fault)"
  - "Type never produced → a severed produces/consumes link (Lab 2's `null` shape)"
  - "Budget stop (`MaxActionsEarlyTerminationPolicy`) → the plan is spinning"
  - "+ since 0.4.0: retriable vs non-retriable exception → transient fault, its own signature"
  - Footer: "Four faults, four log shapes. That's a diagnosis table, not vibes."

## Slide 5 — The rule of the lab
- **Layout:** single sentence template, huge.
- **Visual:** a fill-in-the-blanks sentence card.
- **On-slide copy:**
  - Headline: "Root cause in ONE sentence — before you touch the code."
  - Template: "'[action] produces [state], so [condition] is always false and [goal action] can
    never run.'"

## Slide 6 — The whodunit (demo interstitial)
- **Layout:** near-empty demo slide.
- **Visual:** log panel, live; a magnifying glass motif.
- **On-slide copy:**
  - "`git checkout lab4-broken` · `x \"…\" -p -r`  (keyless: `SPRING_PROFILES_ACTIVE=mock … ` → `plan \"…\"`)"
  - "Find the condition that never flips TRUE. Call it out."
- **Notes:** scroll WITH the room; let someone spot `noDoubleBooking: FALSE` recurring. The mock
  profile reproduces the stall too (`MockModeIntegrationTest` fails the same way), so keyless
  attendees run `SPRING_PROFILES_ACTIVE=mock ./mvnw spring-boot:run` then `plan "…"` — `x` fails
  under the mock, and `-p -r` are `x`-only, but `plan` prints the same stuck planning log. No shell
  at all? `./mvnw test` already prints the world-state (Lab 4 Step 1).

## Slide 7 — The reveal
- **Layout:** one-line diff card.
- **Visual:** the diff: `new ScheduleItem(s, "TBD")` (red) → `new ScheduleItem(s, s.slot())`
  (green). A "compiles fine" chip next to the red line.
- **On-slide copy:**
  - Headline: "Every item pinned to slot \"TBD\" — any two sessions always clash."
  - Sub: "It compiles. It fails at runtime, in the gap between the type system (satisfied) and
    the condition system (never satisfiable). Only the log sees that gap."

## Slide 8 — Your trace is standard-issue
- **Layout:** lineage + compliance pair.
- **Visual:** timeline chips Dapper 2010 → Zipkin 2012 → OpenTelemetry 2019 → GenAI semantic
  conventions; beside it a small "EU AI Act Art. 12 — automatic event logging" card.
- **On-slide copy:**
  - Headline: "Agent traces flow into the SAME observability stack as the rest of your estate."
  - Sub: "'confirm blocked because noDoubleBooking false' is an explanation a regulator — or an
    incident review — can read."
  - Citation line: "Sigelman et al. 2010 · OTel GenAI semconv · EU AI Act Art. 12/86 · NIST AI RMF"
- **Notes:** cash in the M0 compliance promise here.

## Slide 9 — Observability you build (the black-box recorder)
- **Layout:** code + log pair with an event stream between them.
- **Visual:** left: a compact code card — `@Component class PlanFlightRecorder implements
  AgenticEventListener` with `onProcessEvent(...)` visible and the `@Component` annotation
  highlighted (the whole registration story is that one annotation). Centre: an event stream —
  small chips flowing from a platform box into the listener: "plan formulated · action executed
  (+duration) · goal achieved · STUCK". Right: a log panel with the real output line:
  `[flight-recorder] sharp_payne finished — 6 planning cycle(s), 5 replan(s), 6 action run(s)`.
- **On-slide copy:**
  - Headline: "The third surface: any Spring bean implementing `AgenticEventListener` gets
    every event."
  - "The same stream the logging personalities render — plans, actions with durations, goals,
    `STUCK`"
  - "`PlanFlightRecorder` (on `main`): one summary line per run. On `lab4-broken`, the replan
    count IS the diagnosis — one integer."
  - Sub: "No Docker, no keys — it fires even inside the mocked integration tests. Pipe it into
    the observability estate you already run."
- **Notes:** this is where observability stops being something you read and becomes something
  you build; going-further in the worksheet extends it to count LLM requests per action.

## Slide 10 — Foil + lab
- **Layout:** checklist + task card.
- **On-slide copy:**
  - Forecast (Track B): "☐ Paste the stuck log into an agent and let it guess — the guess may
    even be right, but it's unfalsifiable until YOU read the world-state. Evidence vs vibe."
  - Lab: "Reproduce → read the log → (optional) Zipkin via Docker → state the sentence → fix one
    line → prove with the regression test"
  - Acceptance: "you name the failed condition BEFORE editing; goal span completes after"
  - "Worksheet: `labs/lab4-explainability.md` · 40 min"

## Slide 11 — Keystone + transition
- **Layout:** keystone table, row 4 highlighted; ritual footer.
- **On-slide copy:**
  - Row 4 lit: "Explainability · planning log + Zipkin trace · Read the plan, not the vibes"
  - Transition: "You can read the agent's mind. Now make it do MORE — without breaking what it
    already does."
