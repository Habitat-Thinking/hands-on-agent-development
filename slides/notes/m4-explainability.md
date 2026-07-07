# M4 — Explainability: debug by reading state — topic notes & research

> Slot: 13:50–14:50, first module after lunch — a *detective story* format wakes the room up.
> Branches: `lab4-broken` → `lab4-after`. Habit: **Read the plan, not the vibes.**

## The job of this module

Flip the emotional relationship with failure. A stuck agent is not a mystery; it is a **legible**
failure — the world-state names the condition that never went TRUE. The discipline: state the root
cause in one sentence *before* touching code. This is also the only lab that starts from a broken
branch, and the only branch where `./mvnw verify` failing is correct.

## Slide beats

1. **The anti-pattern: prompt-poking.** When an LLM app misbehaves, the reflex is to edit the
   prompt and re-run — statistical debugging of a system you can't see. Cost: hours, and the "fix"
   is a coincidence you can't explain in review.
2. **Two artefacts, two questions.** Planning log = what the planner *believed and chose*
   (cycle by cycle). Trace = what actually *executed* (spans, tokens, time). The gap between them
   is where agent bugs live. Precise question the split enables: *did the planner choose the wrong
   path, or did a chosen action behave wrongly?*
3. **The fault taxonomy slide** (from Lab 4's going-further, promote it to a slide):
   - a **condition that stays FALSE** → a guardrail is refusing (this lab's fault);
   - a **type never produced** → a severed produces/consumes link (the Lab 2 `null` shape);
   - a **budget stop** → the plan is spinning (visible as `MaxActionsEarlyTerminationPolicy`);
   - a **retriable vs non-retriable exception** (0.4.0+) → a transient fault with its own
     retry signature in the log.
   Four failure shapes, four distinct log signatures — that's a diagnosis table, not vibes.
   Demo garnish: run the broken branch under `embabel.agent.logging.personality: severance`
   (or `starwars`) — same world-state evidence, wide-awake post-lunch room.
4. **The rule of the lab:** root cause in one sentence *before* editing. Write the sentence shape
   on the slide: "*[action] produces [state], so [condition] is always false and [goal action] can
   never run.*"
5. **Zipkin path (optional).** `docker compose up -d`, observability profile, port 9411 — watch
   `assembleSchedule` execute repeatedly while the goal span never closes. Say explicitly: the
   planning-log path needs no Docker; nobody is blocked.
6. **Observability you build** (`AgenticEventListener`). The planning log and the trace are
   surfaces Embabel gives you; the third kind you build. Any Spring bean implementing
   `AgenticEventListener` receives every process event — the `@Component` annotation is the
   entire registration story. `main` carries `PlanFlightRecorder`: per-process counters and one
   `[flight-recorder]` summary line; **replans are derived** (cycles beyond the first), so on
   `lab4-broken` one integer is the whole diagnosis. Two beats to land: (a) the logging
   personalities they've been watching all day are implemented as exactly this kind of
   listener; (b) this is the on-ramp for piping plan events into whatever observability estate
   they already run — the maturity model's observability dimension climbs Eyeballs → Captured →
   Instrumented → **Aggregated** → Closed loop, and this listener is the first step off
   Instrumented. Demo: it fires inside the mocked integration tests (`./mvnw verify`, no keys,
   no Docker).
7. **Keystone table callback** — row 4.

## Live demo script — run it as a whodunit

```bash
git checkout lab4-broken
./mvnw test          # watch it fail — this branch is SUPPOSED to fail; say so twice
./mvnw spring-boot:run
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx" -p -r
```

Scroll the world-state lines **with the room**, don't narrate ahead. Let someone spot
`noDoubleBooking: FALSE` recurring after every assembly. Ask the room for the one-sentence root
cause before showing the code. Then reveal the fault — every `ScheduleItem` pinned to slot `"TBD"`,
so any two sessions always clash — and the one-line fix (`new ScheduleItem(s, s.slot())`) plus the
regression test on `lab4-after`. The reveal that a *runtime* stall traces to one line that
*compiles fine* is the beat that justifies the whole observability habit.

## Research & references

- **Dapper → Zipkin → OpenTelemetry lineage.** Google's Dapper paper (Sigelman et al., 2010)
  founded modern distributed tracing; Twitter open-sourced Zipkin (2012); OpenTelemetry (2019,
  OpenTracing + OpenCensus merger) is now the vendor-neutral standard Embabel's observability
  rides on via Micrometer. One slide: your agent traces flow into the *same* observability stack
  as the rest of your estate — agents are not a parallel universe.
- **Observability vs. monitoring.** Charity Majors' framing (control-theory observability:
  infer internal state from outputs): monitoring answers known questions; observability lets you
  ask new ones. A stuck GOAP agent is the ideal observable system — its internal state (the
  world-state) is *literally printed*.
- **GenAI observability is converging on standards.** OpenTelemetry's GenAI semantic conventions
  (attributes for model, tokens, tool calls; in active development 2024–2026) — worth one bullet:
  the span attributes attendees see in Zipkin are becoming standardised, so the ritual transfers
  beyond Embabel.
- **The regulatory cash-in (promised at M0).** EU AI Act Article 12 requires automatic event
  logging for high-risk AI systems, and Article 86 grants a right to *explanation of the role of
  the AI system* in decisions. A planning log that says "confirm blocked because noDoubleBooking
  false" is an explanation a regulator (or an incident review) can read. NIST AI RMF's MEASURE
  function likewise. This is where "explainability" stops being a virtue-word and becomes an audit
  artifact.
- **Blameless-postmortem culture fit.** The one-sentence-root-cause discipline mirrors SRE
  incident practice (Google SRE book, ch. 15): evidence, then hypothesis, then change. Track B's
  paste-the-log-and-let-the-agent-guess is the counter-demo: the guess may even be *right*, but
  it's unfalsifiable until you've read the world-state yourself — the log is evidence; the guess
  is a vibe.
- **"It compiles but stalls" as a category.** The fault lives in the gap between the type system
  (satisfied) and the condition system (never satisfiable). Only the second is visible at runtime
  via the log — a genuinely novel debugging surface for Java developers; name it.

## Track notes

- Track A is mandatory here in spirit: everyone reads the log by eye once. Track C's angle: the
  harness's two-stage review would have flagged a diff that pins slots to a constant — invariants
  make regressions *reviewable*.

## Debrief prompts

- Collect three attendees' root-cause sentences; compare precision. The best ones name action,
  state, condition, and blocked goal.
- "What's your production equivalent of `-p -r`?" — most LLM apps in the room log prompts at best;
  none log *planner beliefs*. Let the discomfort sit.

## Transition out

"You can now read the agent's mind. Time to make it do more — without breaking what it already
does. The test of the seams: extension."
