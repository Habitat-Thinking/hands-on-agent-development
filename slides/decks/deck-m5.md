# Deck M5 — Extend without breaking (10 slides)

> Shown before Lab 5, the capstone lab (longest hands-on block). Habit badge: "Habit 6 — Extend
> by adding, not rewiring". Speaker notes: `../notes/m5-extend.md`.

## Slide 1 — Module title
- **Layout:** section title.
- **Visual:** the plan ribbon duplicating: a second, parallel ribbon fading in beneath it ending
  in a new `planNetworking` node.
- **On-slide copy:**
  - "M5 — Extend without breaking"
  - "Good architecture is measured at extension time."

## Slide 2 — The test of the seams
- **Layout:** provocation slide.
- **Visual:** the schedule pipeline with `assembleSchedule`, `confirmSchedule` and the guardrails
  wrapped in a "DO NOT TOUCH" tape band.
- **On-slide copy:**
  - Headline: "Add a networking plan. Touch nothing the schedule depends on."
  - Sub: "If the feature forces you inside that tape, the seams were in the wrong place."

## Slide 3 — Two constraints shape the design
- **Layout:** two framework-truth cards (discovered, not decreed).
- **Visual:** card 1: one agent, one goal star; card 2: an agent's plan space drawn as a fence
  around only its own actions. Both stamped "found by reading the planning log".
- **On-slide copy:**
  - "1 — An `@Agent` supports ONE goal type. `PersonalSchedule` is taken → `NetworkingPlan`
    needs its own agent."
  - "2 — An agent plans ONLY with its own actions. No borrowing; `@EmbabelComponent` actions are
    NOT pooled."
  - Sub: "So the new agent must declare its own upstream steps. Copy-paste? No —"

## Slide 4 — Share logic, not goals
- **Layout:** the design diagram (centrepiece).
- **Visual:** amber `@Service ConfPlanningCapabilities` box in the centre; two blue agents above
  and below, each with thin one-line `@Action` wrappers arrowing into the service. Label the
  wrappers "one-liners".
- **On-slide copy:**
  - Headline: "Pipeline LOGIC in a plain `@Service`; each agent declares thin wrappers."
  - Sub: "Shared logic, per-agent actions. Spring's oldest trick is the extension seam for
    agents too."

## Slide 5 — Why one-goal-per-agent is a feature
- **Layout:** consequence slide.
- **Visual:** two small readable planning logs vs one giant tangled one (crossed out).
- **On-slide copy:**
  - Headline: "Every action widens the plan space and lengthens the trace you must read."
  - Sub: "Small agents keep the M4 habit possible. The God agent is the LLM-era God object."

## Slide 6 — This principle has a name
- **Layout:** lineage bridge.
- **Visual:** Parnas 1972 card + Open-Closed card → the service diagram from slide 4.
- **On-slide copy:**
  - Headline: "Open for extension, closed for modification — enforced by a planner, not a code
    review."
  - Sub: "You CAN'T tweak another agent's flow: plans only route through an agent's own typed
    actions. The principle became a physical property."
  - Citation line: "Parnas 1972 · Meyer/OCP 1988"

## Slide 7 — The proof is the regression
- **Layout:** evidence slide.
- **Visual:** test-suite panel: existing schedule integration test + `GuardrailEnforcementTest`
  with green ticks THROUGHOUT; a caption timeline "before / during / after the change".
- **On-slide copy:**
  - Headline: "'Provably untouched' is a TEST claim, not a diff claim."
  - Sub: "The only schedule-path edit is moving prompt bodies into the service — same plan, same
    output, green the whole time."

## Slide 8 — Foil + lab
- **Layout:** checklist + task card.
- **On-slide copy:**
  - Forecast (Track B): "☐ Bolts networking onto the schedule goal ☐ Edits `assembleSchedule`
    to also emit people ☐ Copy-pastes the pipeline into a second class"
  - Lab: "Add `NetworkingPlan` → extract the `@Service` → make `ConfPlannerAgent` delegate →
    add `ConfNetworkingAgent` with its own wrappers + `planNetworking` goal"
  - Acceptance: "networking log shows the reused pipeline; schedule flow and ALL its tests
    unchanged"
  - "Worksheet: `labs/lab5-extend.md` · 45 min · Track C preferred"

## Slide 9 — If ConfPlanner grew (planner-choice aside)
- **Layout:** three mini-scenario cards (ties back to M2 slide 8).
- **On-slide copy:**
  - Headline: "Extension also means choosing a planner on purpose."
  - "Preferences evolving across a multi-day conference → Utility · Registration wizard with
    human confirmation → `@State` · Open-ended concierge → Supervisor"
  - Sub: "Same typed actions underneath all of them."

## Slide 10 — Keystone + transition
- **Layout:** keystone table, row 5 highlighted; ritual footer.
- **On-slide copy:**
  - Row 5 lit: "Extend without breaking · new agent/action, flow unchanged · Extend by adding,
    not rewiring"
  - Transition: "Two agents, five LLM calls per run — some of them pulling five fields out of a
    sentence with your most expensive model. Last habit: right-size it."
