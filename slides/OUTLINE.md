# Slide spine — Hands-On AI Agent Engineering

Module-by-module. Each module: a short frame, a live demo on the matching branch, then attendees
work the lab in their chosen track. The keystone slide is the objective→mechanism→habit table (M0),
referenced back at every module.

> Deck-building material lives in two layers: [decks/](decks/README.md) holds the
> slide-by-slide build specs for Claude Design (layouts, visuals, verbatim copy, shared design
> system); [notes/](notes/README.md) holds the per-module speaker notes, demo scripts, research
> citations, and Q&A prep, alongside the [gaps-and-extensions](notes/gaps-and-extensions.md)
> review of the workshop against the current Embabel state of play.

## M0 — Orientation (frame the day)

- Why agents fail in production: ungrounded domains, hidden steps, no contract, no trace.
- **Two crafts hide inside "AI agents"**: *agentic building* (agents as your workforce — agentic
  workflows, harnesses, habitat thinking; *The Sovereign Engineer*) vs *building agents* (the
  agent as your product — Embabel). Declare it: today is mostly building agents, driven via
  agentic building (Track C).
- **The dual harness**: build-time (`ai-literacy-superpowers`) and runtime (Embabel) teach the same
  three disciplines — context engineering, architectural constraints, guardrail design. The amber
  column is the agentic-building craft; the blue column is the building-agents craft.
- **Two ladders**: operational (what the habitat delivers — Dictating → Commanding → Regulating
  → Orchestrating → Supervising; the Agentic Experience maturity model's agent-behaviour
  dimension) and cognitive (what you can think and do — Aware → Prompter → Verifier → Habitat
  Engineer → Specification Architect → Sovereign Engineer; *The Sovereign Engineer*). The gap
  between them is the Habitat Build Gap; coherence is the signal. Sources in `reference/`.
- **Keystone slide** — the table:

  | # | Objective | Embabel mechanism | Habit |
  |---|---|---|---|
  | 1 | Typed domain models (DICE) | `PromptContributor` on the domain | Model the domain first |
  | 2 | Goal-oriented behaviour | GOAP re-derives the plan | Name the goal, not the steps |
  | 3 | Preconditions & invariants | `@Condition` gate + budget + secured tool | Make the contract explicit |
  | 4 | Explainability | planning log + Zipkin trace | Read the plan, not the vibes |
  | 5 | Extend without breaking | new agent/action, flow unchanged | Extend by adding, not rewiring |
  | 6 | Right-size the model | `withLlmByRole` cheap-vs-strong | Right-size the model |

- Demo: `main` — run `x "..." -p` and read the inferred plan. (Keyless venue: run under
  `SPRING_PROFILES_ACTIVE=mock` and invoke with `plan "..."` — `x` ranks via the LLM and fails under
  the mock; `plan` invokes the goal directly and prints the plan. See `docs/how-to/use-mock-mode.md`.)

## M1 — DICE: behaviour on domain objects (Lab 1)

- Domain-in-context-engineering: put the rule *on* `AttendeeProfile`, not in every prompt.
- Demo: `lab1-before` → add `avoidTopics` + `PromptContributor` → `lab1-after`; show the avoid-list
  in the prompt log.
- Lab + three-track note (Track A/B contrast is sharp here: B forgets the test).

## M2 — GOAP: name the goal, not the steps (Lab 2)

- Plans are derived from types, not written. Add an action and watch the plan grow.
- Demo: `lab2-before` → add `researchSessions` → `lab2-after`; diff the planning log. Return `null`
  from research → watch replanning.

## M3 — Guardrails: make the contract explicit (Lab 3)

- Four shapes: invariant (postcondition the goal depends on), precondition, budget, access control.
- The subtle truth: a postcondition alone doesn't gate — the **goal must require** the invariant.
- Demo: `lab3-before` → split assemble/confirm + `noDoubleBooking` + budget + `@SecureAgentTool` →
  `lab3-after`; show a clashing draft refused.

## M4 — Explainability: debug by reading state (Lab 4)

- A stuck agent is a legible failure. Read the world-state; find the condition that stayed false.
- Demo: `lab4-broken` — run it, watch the plan stall (`MaxActionsEarlyTerminationPolicy`; the word
  `STUCK` itself shows only in the `[flight-recorder]` summary line); read the planning log (and Zipkin if Docker is up);
  state the root cause; `lab4-after` fixes one line + a regression test.

## M5 — Extend without breaking (Lab 5)

- New capability = new agent/action, existing flow untouched. Two Embabel constraints (one goal per
  agent; agents plan only with their own actions) → share *logic* via a `@Service`, not actions.
- Demo: `lab5-before` → add `ConfNetworkingAgent` reusing the pipeline → `lab5-after`; schedule
  regression stays green.

## M6 — Model routing: the cheapest model that passes (Lab 6)

- Reframe (lead-in slide): the context window is one finite budget, and the day's habits are its
  levers — DICE fills it, guardrails cap it, routing prices it, RAG shrinks it. Names the resource
  "context engineering" (M0) was managing all along.
- Cost is a design parameter. Route per action by return-type complexity; config, not code.
- The regulated-environment lever: a local model where data can't leave the building.
- Demo: `lab6-before` → `withLlmByRole` cheap/strong + `MODEL_ROUTING.md` → `lab6-after`. (Needs a
  real key — the mock spends no tokens, so there are no cost lines to compare.)

## Wrap — Govern the loop

- The eight-habit stack (`HABITS.md`); habits 7–8 (test the seams, govern the loop) run throughout.
- Track C recap: you built the agent using the disciplines the agent embodies.
- Where next, two distinct horizons: agentic RAG (`ToolishRag`) shrinks the within-a-call window;
  cross-turn memory (`embabel-chat-store`, `@State`) extends the agent across turns — ConfPlanner is
  single-turn by design.
- The ritual to take home: read the plan, read the trace, confirm the acceptance check.
