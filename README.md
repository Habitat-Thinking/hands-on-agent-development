# Hands-On AI Agent Engineering

> Habits for safe, explainable, domain-grounded AI — an UberConf full-day workshop.

You will build **ConfPlanner**: a goal-oriented [Embabel](https://github.com/embabel/embabel-agent)
agent (Java) that turns a free-text request — *"I'm a senior platform engineer into Kubernetes,
resilience and DevEx; build me a schedule"* — into a **conflict-free personal conference schedule**.
The agent is about the very conference you're sitting in. The catalog is synthetic and fictional.

You learn by **operating git branches**: check out a `*-before` branch, implement the change with
its worksheet, then diff against the matching `*-after` branch — or just walk the `*-after` branches
to watch the finished progression.

## The dual harness (the through-line)

You work in two harnesses at once, and they teach the same three disciplines —
**context engineering, architectural constraints, guardrail design** — at two altitudes:

| | Build-time harness | Runtime harness |
|---|---|---|
| What | `ai-literacy-superpowers`: orchestrator → implementer, `HARNESS.md` / `AGENTS.md` / `MODEL_ROUTING.md`, constraint-gate, two-stage review | Embabel: DICE domain models, GOAP planning, invariants, traces |
| You drive it by | climbing the ladder: Dictating → Commanding → Regulating → Orchestrating → Supervising | reading the plan and the trace, not the vibes |

The punchline you'll feel by the end: **Track C builds the Embabel agent using the very disciplines
the Embabel agent itself embodies.** The harness is a planning loop for your engineering.

## What you'll build, lab by lab

| # | Objective | Embabel mechanism | Habit | Lab |
|---|---|---|---|---|
| 1 | Typed domain models (DICE) | `PromptContributor` on `AttendeeProfile` | Model the domain first | `labs/lab1-dice.md` |
| 2 | Goal-oriented behaviour (GOAP) | add `researchSessions`; the plan re-derives | Name the goal, not the steps | `labs/lab2-goap.md` |
| 3 | Preconditions & invariants | `noDoubleBooking` gate + precondition + budget + `@SecureAgentTool` | Make the contract explicit | `labs/lab3-guardrails.md` |
| 4 | Explainability | planning log + Zipkin trace; debug a stuck plan | Read the plan, not the vibes | `labs/lab4-explainability.md` |
| 5 | Extend without breaking | add a `NetworkingPlan` agent; schedule flow unchanged | Extend by adding, not rewiring | `labs/lab5-extend.md` |
| 6 | Right-size the model | `withLlmByRole` cheap-vs-strong; `MODEL_ROUTING.md` | Right-size the model | `labs/lab6-model-routing.md` |
| 7 (stretch, take-home) | Agentic RAG over the catalog | retrieval as a tool feeding the plan | Extend by adding, not rewiring | `labs/lab7-rag.md` |

See **[HABITS.md](HABITS.md)** for the full eight-habit stack.

## Documentation

The full workshop documentation — tutorials, how-to guides, reference, and explanation — is published
at **<https://habitat-thinking.github.io/hands-on-agent-development/>** (source in [`docs/`](docs/)).

To build and serve the docs site locally:

```bash
pip install -r docs/requirements.txt
mkdocs serve      # then open http://127.0.0.1:8000
```

## Prerequisites

- **Java 21+** and the bundled Maven wrapper (`./mvnw`). No system Maven needed.
- A model provider key in `.env` — **OpenAI** by default (`OPENAI_API_KEY`) or Anthropic. The
  **build and all tests are green with no key** (the test harness mocks the LLM); you only need a
  key to actually *run* the agent against a real model.
- Optional: **Docker** for the Lab 4 Zipkin trace and any MCP tools. The default lab path needs none.
- `gh` CLI if you want to fork/clone via GitHub.

See **[SETUP.md](SETUP.md)** for keys, the mock-mode profile, the no-Docker fallback, and Codespaces.

## Quick start

```bash
./mvnw clean verify           # builds green, runs the mocked tests — no keys required
cp .env.example .env          # then add a provider key to actually run the agent
./mvnw spring-boot:run        # starts the Embabel shell
# in the shell:
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"
x "..." -p -r                 # add prompt (-p) and result (-r) verbosity to read the plan
```

## How to operate the labs by branch

```bash
git checkout lab1-before                 # the starting point + worksheet + TODO anchors
$EDITOR labs/lab1-dice.md                # do the lab
./mvnw -q verify                         # check your work
git diff lab1-before lab1-after -- src   # compare against the reference solution
```

### The walk order

```
lab1-before → lab1-after
lab2-before → lab2-after
lab3-before → lab3-after
lab4-broken → lab4-after        ← Lab 4 starts from a deliberately broken plan
lab5-before → lab5-after
lab6-before → lab6-after
main                            ← all labs applied + the workshop docs
```

> `lab4-broken` is the one branch that intentionally **fails** `./mvnw verify`: it compiles but the
> agent goes `STUCK` at runtime. That failing run is exactly what Lab 4 teaches you to diagnose.

## Three tracks, every lab

Each lab is the same change, three ways — switch tracks as the day goes on (see each worksheet):

- **Track A — by hand.** Edit Java, run the shell, read the plan. The ground truth.
- **Track B — coding agent, ungoverned.** A teaching foil: watch it guess the domain and skip the test.
- **Track C — through the `ai-literacy-superpowers` harness (preferred).** Orchestrator plans it, the
  Java implementer makes the edit, the constraint-gate and two-stage review gate it. You supervise.

Close every lab with the ritual: **read the planning log, read the trace, confirm the acceptance check.**

## Project layout

```
src/main/java/com/russmiles/confplanner/
  domain/        typed records (AttendeeProfile, Session, PersonalSchedule, …)
  service/       CatalogService — plain code over the synthetic catalog
  agent/         ConfPlannerAgent, ConfNetworkingAgent, ConfPlanningCapabilities
  shell/         ConfPlannerShell — run a goal programmatically
src/main/resources/catalog/uberconf-sample-catalog.json   ← SYNTHETIC, fictional
labs/            one worksheet per lab
quizzes/         one short section quiz per module (M0–M7) — the tutor administers them
slides/OUTLINE.md  module-by-module slide spine
HABITS.md · SETUP.md · MODEL_ROUTING.md
CLAUDE.md · AGENTS.md · HARNESS.md · REFLECTION_LOG.md     ← build-time harness
```

Built with Embabel 0.5.0 on Java 21. The catalog is entirely invented — no real UberConf schedule or
real people's abstracts are reproduced.
