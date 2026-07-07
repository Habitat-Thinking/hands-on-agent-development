# M5 — Extend without breaking (capstone lab) — topic notes & research

> Slot: 15:00–16:10 (15 min frame, 45 min lab — the longest; 10 min debrief).
> Branches: `lab5-before` → `lab5-after`. Habit: **Extend by adding, not rewiring.**

## The job of this module

The capstone question: *how does a well-modelled agent grow?* Answer: new capability arrives as new
types/actions — here, a whole new **agent** — and the existing flow is provably untouched (its
regression tests never went red). Two real Embabel 0.5.0 constraints shape the design, and both
were discovered by reading the planning log, which makes them teachable rather than arbitrary.

## Slide beats

1. **The test of the seams.** If adding networking forces you to touch `assembleSchedule`,
   `confirmSchedule`, or any guardrail, the seams were in the wrong place. Good architecture is
   measured at extension time, not at design time.
2. **Two constraints, one design** (the framework-truth slide):
   - An `@Agent` supports **one goal type**. `PersonalSchedule` is taken → `NetworkingPlan` needs
     its own agent.
   - An agent plans **only with its own actions** — it cannot borrow another agent's, and
     `@EmbabelComponent` actions are *not* pooled into another agent's planning.
   Consequence: the new agent must declare its own upstream steps. The naive reading is
   "copy-paste the pipeline"; the design answer is better —
3. **Share logic, not goals.** Pipeline *logic* moves to a plain `@Service`
   (`ConfPlanningCapabilities`); each agent declares thin one-line `@Action` wrappers that
   delegate. Shared logic, per-agent actions. Spring's oldest trick — constructor-injected
   services — is the extension seam for agents too.
4. **Why one-goal-per-agent is a feature.** Every action added widens the plan-search space and
   lengthens the trace you must read. Small agents = readable planning logs = the M4 habit stays
   possible. (Connect explicitly: extensibility here is in service of explainability.)
5. **The proof is the regression.** The schedule flow's integration test and
   `GuardrailEnforcementTest` stay green throughout; the only schedule-path edit is moving prompt
   bodies into the service — same plan, same output. "Provably untouched" is a *test claim*, not a
   diff claim.
6. **Keystone table callback** — row 5 (habit 6).

## Live demo script

```bash
git checkout lab5-after
./mvnw spring-boot:run
# schedule goal — unchanged:
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule" -p
# networking goal — new agent, reusing the same pipeline via the service:
# (invoke the NetworkingPlan goal; planning log shows extract → loadCatalog → shortlist → research → planNetworking)
./mvnw -q verify     # every pre-existing test still green — the regression IS the demo
```

Show both planning logs side by side: same first four steps (delegating to one service), different
goals. Then show `ConfPlannerAgent`'s actions are now one-liners — the diff that *looks* biggest
changed behaviour not at all.

## Research & references

- **Open–Closed Principle.** Meyer (*OOSC*, 1988) via Robert Martin's restatement: open for
  extension, closed for modification. This lab is OCP with a planner as the enforcement mechanism:
  you *can't* "just tweak" another agent's flow, because plans only route through an agent's own
  typed actions. The framework constraint turns a principle into a physical property.
- **Parnas (1972), *On the Criteria To Be Used in Decomposing Systems into Modules***: modules
  should hide decisions likely to change. The `@Service` hides *how* shortlisting/research work;
  agents hide *what goal* they pursue. When the criteria are right, a feature = a new module —
  which is exactly what attendees just did.
- **Conway/team-shaped framing** (Team Topologies, Skelton & Pais, 2019 — optional): one goal per
  agent mirrors one stream-aligned team per product; the shared `@Service` is the platform. For
  org-minded audiences this lands the multi-agent story as an org-design story.
- **Multi-agent industry context.** 2024–2026 moved from monolithic agents to *composed* systems —
  Anthropic's multi-agent research system write-up, A2A (agent-to-agent) protocol efforts, MCP as
  the tool seam. Embabel's one-goal-per-agent + typed handoffs is the disciplined end of that
  spectrum. (The gaps doc covers what newer Embabel adds here — delegation/`@EmbabelComponent`
  evolutions; check it before presenting so this slide matches the pinned version's truth.)
- **The anti-pattern has a name: the God agent.** One agent, fifteen tools, one mega-prompt —
  the LLM-era God object. Same failure signature: untestable, unexplainable, every change risks
  everything. One-goal agents are the refactoring.
- **Regression as the definition of "didn't break".** Feathers' *Working Effectively with Legacy
  Code* (2004): tests are what make change safe; a seam is where you can alter behaviour without
  editing code. The mocked-LLM action boundary is the seam; the untouched green suite is the
  contract that "extend" ≠ "rewire."

## Track B foil — predict out loud

The ungoverned agent asked to "add networking" tends to do one of: bolt networking onto the
schedule goal (violates one-goal), edit `assembleSchedule` to also emit people (rewiring), or
copy-paste the whole pipeline into a second class (duplication instead of a service). Score it
against the constraint: *capability via new types/actions only; never hard-wire flow order*.

## Debrief prompts

- "What did you have to understand before the design made sense?" (Usually: that
  `@EmbabelComponent` actions aren't pooled — a discovered constraint, found by reading the log.
  Reinforce: the *log* is how you learn a framework's real semantics.)
- "Which of your production features, added last quarter, was a rewire that should've been an add?"

## Going-further threads

- Compose Lab 3 + Lab 5: `planNetworking` reads the secured `PremiumBriefing` when authorised.
- One shell command that invokes `PersonalSchedule` then `NetworkingPlan` — two goals, one UX.

## Transition out

"Two agents, five LLM calls per run. Some of those calls are pulling five fields out of a sentence
with your most expensive model. Last habit: right-size it."
