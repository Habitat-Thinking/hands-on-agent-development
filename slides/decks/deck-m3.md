# Deck M3 — Guardrails: make the contract explicit (11 slides)

> Shown before Lab 3 (pre-lunch). Habit badge: "Habit 3 — Make the contract explicit".
> Speaker notes: `../notes/m3-guardrails.md`. The trap slide (5) is the most important slide of
> the day — give it room.

## Slide 1 — Module title
- **Layout:** section title.
- **Visual:** plan ribbon; a padlock forming between `assemble` and a new `confirm` node.
- **On-slide copy:**
  - "M3 — Guardrails: preconditions & invariants"
  - "Delete the word 'hope' from the sentence."

## Slide 2 — A request is not a guarantee
- **Layout:** quote-and-consequence.
- **Visual:** the actual assemble prompt line "Hard rule: never pick two sessions in the same
  slot" in a prompt card; behind it, faded, a stack of runs with one red clashing run.
- **On-slide copy:**
  - Headline: "The prompt already says 'never double-book.' A prompt is a request."
  - Sub: "Honoured 99% of the time × thousands of runs = a steady stream of broken schedules."
- **Notes:** callback to the 0.95¹⁰ slide.

## Slide 3 — Four shapes of guardrail
- **Layout:** four-row table (worksheet table, styled).
- **Visual:** rows with icons: invariant (shield), precondition (gate), budget (meter),
  access control (key). Third column "Reassessed…" emphasised.
- **On-slide copy:**
  - "Invariant `noDoubleBooking` · `@Condition` + a goal that REQUIRES it · every planning cycle"
  - "Precondition `hasCandidates` · `@Condition` as `pre` · before the action runs"
  - "Budget · `Budget(0.50, 20, 200_000)` via `ProcessOptions` · continuously"
  - "Access control · `@SecureAgentTool` SpEL authority · before the tool runs"
- **Notes:** 'reassessed-when' is what makes these guardrails, not assertions.

## Slide 4 — …and a fifth
- **Layout:** single addition card (NEW — 0.5.0 content).
- **Visual:** a fifth row sliding under the slide-3 table, tinted differently: content (filter
  icon).
- **On-slide copy:**
  - "Content guardrail · `UserInputGuardRail` / `AssistantMessageGuardRail` · on every LLM
    exchange"
  - Sub: "Shapes 1–4 guard the PLAN. This one guards the CONTENT — deterministically, before any
    spend. Worked example on `main`: `RequestContentGuardRail` rejects prompt-injection markers."
- **Notes:** going-further in the lab; test proves it with no keys.

## Slide 5 — THE TRAP (the day's most important slide)
- **Layout:** full-slide code card with a big verdict stamp.
- **Visual:** the natural-but-wrong code, large; a red stamp across it.
- **On-slide copy:**
  - Code: `@AchievesGoal @Action(post = {"noDoubleBooking"}) PersonalSchedule
    assembleSchedule(...)`
  - Stamp: "LOOKS LIKE IT ENFORCES. IT DOES NOT."
  - Sub: "A `post` is a PLANNING PROMISE — the goal is 'achieved' the moment the output type
    exists, whether or not the post holds."
- **Notes:** not a bug; `post` exists so the planner can chain. Gating is a different question.

## Slide 6 — The fix: make the goal REQUIRE it
- **Layout:** three-step vertical diagram.
- **Visual:** assemble (produces `DraftSchedule`, tagged `post=noDoubleBooking, canRerun`) →
  condition diamond (green/red) → confirm (tagged `pre=noDoubleBooking`, goal star). Failure
  loop drawn back from red diamond to assemble, ending at a budget meter.
- **On-slide copy:**
  - Headline: "Split assemble → confirm. The invariant becomes a precondition the goal depends on."
  - "Clashing draft → condition FALSE → confirm can't run → planner re-runs assembly → budget
    stops it VISIBLY"
  - Mantra bar: "To make an invariant bite, the goal must require it as a `pre` — not promise it
    as a `post`."
- **Notes:** mirrors Embabel's own Researcher example.

## Slide 7 — Design by Contract, one altitude up
- **Layout:** lineage bridge.
- **Visual:** Meyer card (1992) + Hoare triple `{P} C {Q}` chip → the pre/post/`@Condition`
  annotations.
- **On-slide copy:**
  - Headline: "Preconditions, postconditions, invariants — you've met these. Now they're
    plan-space."
  - Sub: "The twist: here `post` informs the planner; only `pre` on the goal is a wall."
  - Citation line: "Meyer, 'Applying Design by Contract', IEEE Computer 1992 · Hoare 1969"

## Slide 8 — Budget: the floor under everything
- **Layout:** single mechanism card.
- **Visual:** budget meter with three gauges: $0.50 · 20 actions · 200k tokens; a retry spiral
  being cut short.
- **On-slide copy:**
  - Headline: "The budget turns 'retry until it works' into 'retry, bounded, then stop visibly.'"
  - "`ProcessOptions.DEFAULT.withBudget(new Budget(0.50, 20, 200_000))`"
  - Sub: "The agent-era circuit breaker — the stop is a log line, not a mystery (Nygard,
    *Release It!*)."

## Slide 9 — Deny before you spend
- **Layout:** single mechanism card.
- **Visual:** key icon in front of a premium action node sitting OFF the plan ribbon's main path.
- **On-slide copy:**
  - Headline: "`@SecureAgentTool(\"hasAuthority('conf:premium')\")` — refused before any LLM
    spend."
  - Sub: "Design note: `PremiumBriefing` is a side type the goal never consumes — access control
    without touching the free flow."

## Slide 10 — Foil + lab
- **Layout:** checklist + task card.
- **On-slide copy:**
  - Forecast: "☐ Strengthens the prompt ('IMPORTANT: never…') — hope, capitalised ☐ Adds a
    silent `if` that hides the violation instead of surfacing it"
  - Lab: "Add `DraftSchedule` → two `@Condition`s → split assemble/confirm → budget → secured
    tool"
  - Acceptance: "a clashing draft NEVER reaches the goal (`GuardrailEnforcementTest`) · empty
    shortlist fails cleanly · budget stop visible in the log"
  - "Worksheet: `labs/lab3-guardrails.md` · 35 min"

## Slide 11 — Keystone + transition (to lunch)
- **Layout:** keystone table, row 3 highlighted; ritual footer.
- **On-slide copy:**
  - Row 3 lit: "Preconditions & invariants · `@Condition` gate + budget + secured tool · Make the
    contract explicit"
  - Transition: "You've built an agent that would rather stop than lie. After lunch: what
    stopping looks like — and how to read WHY from the log instead of guessing."
