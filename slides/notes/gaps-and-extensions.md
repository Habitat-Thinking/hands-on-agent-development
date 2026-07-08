# Gaps & extensions — the workshop vs. the Embabel state of play

> Researched 6 July 2026 against GitHub releases, Maven Central metadata, the 0.5.0 user guide,
> and Rod Johnson's essays. Verdict up front, then gaps ranked by teaching value, then a proposed
> upgraded day plan, then a maintenance risk register.

## Verdict: the pin is current; the gaps are content, not version

**Embabel 0.5.0 "Darwin" (released 21 June 2026) is the latest published release.** The workshop
is not behind — it is, pleasingly, *ahead of Embabel's own `java-agent-template`* (still pinned at
0.3.5). The real gaps are:

1. **Unexercised 0.3.x–0.5.0 features** the labs never touch (planner breadth, the guardrail
   framework, cost tracking, RAG, streaming).
2. **The horizon**: a 1.0.0-RC1 dev cycle is underway on `main` (deprecated APIs purged
   30 June 2026), and a 2.0 branch targets Spring Boot 4 / Spring AI 2.0 / Jackson 3.

Version timeline that matters for slides: 0.1.0 Aug 2025 → 0.3.3 (own tool-calling loop +
**guardrail framework**, Jan 2026) → 0.4.0 "Curdimurka" (**global guardrails, action retry,
per-call cost tracking, prompt caching, streaming**, May 2026) → 0.5.0 "Darwin" (**native OpenAI
structured output, four documented planner styles**, June 2026) → 1.0.0-RC1-SNAPSHOT on `main`.
Releases carry alphabetical Australian place-name codenames — a fun M0 footnote.

---

## Gap analysis, ranked by teaching value

### Gap 1 — Planner breadth (high value, low cost) → M2

The workshop teaches GOAP as *the* Embabel planner. As of 0.5.0 the docs describe **four planner
styles under one programming model**: A* GOAP, a **Utility AI planner** (value-based, for when
there's no fixed goal), a **Supervisor/LLM planner** (type-informed LLM tool selection with
precondition validation), and **`@State` typed state machines** (branching, looping, HITL). This
is Rod Johnson's current headline differentiator vs LangGraph — a workshop that presents GOAP as
the whole story slightly undersells the framework *and* misses a great teaching beat: **the
conditions/types discipline the day teaches is what makes all four planners possible.**

**Extension:** one new M2 slide ("GOAP is one of four — you're learning the one that's most
legible; the discipline transfers") — already noted in `m2-goap.md`. Optional going-further for
Lab 5: sketch when you'd pick Utility or `@State` for ConfPlanner (e.g. a multi-day conference
with evolving preferences = utility; a registration wizard = `@State`). No code change needed.

### Gap 2 — The guardrail *framework* and global guardrails (high value) → M3

Lab 3 teaches guardrails via conditions/budget/`@SecureAgentTool` — all still correct. But since
0.3.3 Embabel also ships a first-class **guardrail framework** for user input and LLM responses,
with **global guardrails configuration** in 0.4.0 and a documented "Budget Guardrail" pattern in
the 0.5.0 guide. The lab's four shapes are *plan-level* guardrails; the framework adds
*content-level* ones (screen inputs/outputs across every agent, not per-action).

**Extension:** add a fifth row to the M3 four-shapes table — "Content guardrail (screen
input/output) | guardrail framework, global config | on every LLM exchange" — and a going-further
step: add an input guardrail that rejects requests naming real attendees (nice privacy tie-in with
the synthetic-catalog stance). Medium effort: one new lab step + test on `lab3-after`.

### Gap 3 — Cost observability: per-call cost tracking (high value, tiny cost) → M6

Lab 6's "measure before optimising" beat says *compare the budget line in the log*. Since 0.4.0
Embabel tracks **cost per LLM call** and aggregates costs across nested subprocesses, plus a
**TokenCounter SPI** (promoted stable post-0.5.0) and **Anthropic prompt caching** support. The
routed-vs-all-`best` comparison can now be shown in *dollars per action*, which is a far more
compelling demo than token counts.

**Extension:** update the M6 demo to point at per-call cost lines; add a `MODEL_ROUTING.md` column
for observed cost per action. Prompt caching is one bonus slide: "the third lever after routing
and budgets."

### Gap 4 — Resilience primitives: retry with exception classification (medium value) → M3/M4

0.4.0 added **action retry with retriable/non-retriable exception classification**, **ephemeral
agent processes**, and richer **early-termination** hooks (0.3.5). Lab 3's `canRerun` + budget
story is the planner-level version; the retry/exception machinery is the action-level version —
the agent-era equivalent of the *Release It!* patterns M3's notes already cite.

**Extension:** a going-further in Lab 4: make `researchSessions` throw a transient exception and
watch retry classification handle it vs. a non-retriable one that surfaces immediately. Distinct
log signature → extends the M4 fault taxonomy from three shapes to four.

### Gap 5 — Agentic RAG: `ToolishRag` + chat store (high value as a stretch lab) → new Lab 7

The biggest *capability* gap. Embabel modularised RAG in 0.3.0: **`ToolishRag`** exposes search
operations as LLM tools with auto-disambiguation; Lucene backend is stable (Neo4j/pgvector
incubating); `embabel-chat-store` (0.3.4) persists multi-turn sessions. ConfPlanner's catalog is
currently loaded whole and stuffed into prompts — fine for ~30 sessions, and honestly the right
call pedagogically, but the moment someone asks "what about 500 sessions?" the answer is RAG, and
the workshop has no card to play.

**Extension (stretch Lab 7, optional/"going further after the day"):** index the session catalog
abstracts into Lucene; give `shortlistSessions` (or a new action) `ToolishRag` search instead of
the full menu; watch the prompt shrink and the token budget drop — which *retroactively pays off
Lab 6*. Real work (~a day to build + test), but it would give the workshop a second-day or
follow-up-webinar asset. Reference: Jettro Coenradie's "Agentic RAG with Embabel" walkthrough.

### Gap 6 — Streaming + thinking blocks (low-medium value, demo garnish) → M0/M6

0.4.0 added vendor-neutral streaming; 0.5.0 added thinking-aware streaming converters and
**native OpenAI structured output** (which quietly strengthens Lab 1's typed-output story — the
`creating(T.class)` contract now rides provider-native structured output on OpenAI).

**Extension:** one sentence in M1's structured-output bullet (done — the idiom is unchanged);
optionally stream the assemble step in the M0 opener for stage presence. Skip if time is tight.

### Gap 7 — Ecosystem on-ramps: Agent Skills, Embabel Hub, the Guide MCP server (low cost) → M7

Students will encounter these the week after: `embabel-agent-skills` (0.3.1, tracks the emerging
Agent Skills standard), **Embabel Hub** (hub.embabel.com — talk-to-the-docs agent), and the
**Guide project** — an MCP server that gives RAG-powered Embabel help *inside Claude
Code/Desktop/Cursor*. That last one is a gift for this workshop specifically: Track C attendees
can wire the Embabel Guide MCP server into the same Claude Code they're using as the build-time
harness — the dual-harness story eating its own dog food.

**Extension:** add all three to M7's "where to go next" slide; add an optional SETUP.md appendix
for wiring the Guide MCP server into Claude Code. Also worth listing the flagship examples
(coding-agent — dogfooded by the Embabel team, Tripper, urbot) as "what big looks like."

### Gap 8 — The fun one: logging personalities (zero cost, real energy value) → M4

`embabel.agent.logging.personality` now supports `default | starwars | severance | hitchhiker |
montypython | colossus`. The repo's `application.yml` already winks at `starwars`.

**Extension:** run the post-lunch M4 broken-agent demo with `personality: severance` or
`starwars` — a stuck plan narrated by Vader is exactly the right 13:50 energy. One yml line.

### Gap 9 — Context window & memory: name the resource, name the horizon (high value, near-zero cost) → M6 + M7

Two related things hide under "context and memory," and they want opposite treatment.

**(a) The context window as an engineered budget — reframe, not new content.** "Context engineering"
is one of the day's three named disciplines (M0), and the *techniques* are taught all day — DICE says
the domain rule once instead of per prompt (Lab 1), the Budget guardrail caps the spend (Lab 3),
routing prices each step (Lab 6), RAG shrinks the menu (Lab 7) — but the **window is never named as
the single finite resource all four levers act on.** Each lab manages the window without telling the
learner that's what they're doing. The fix is one synthesis slide, best placed as an M6 lead-in
(the module is already about tokens/cost/budget, and by M6 the room has *lived* three of the four
levers): "the window is the budget, not a container — DICE fills it, guardrails cap it, routing
prices it, RAG shrinks it." It retroactively ties four labs together for near-zero cost.

**(b) Cross-turn memory / statefulness — keep out of core, promote from deflection to a named
horizon.** Multi-turn memory is a genuinely *different* agent shape (ConfPlanner is single-turn goal
planning by design), and adding it would break the fixed branch contract. But Embabel ships
`embabel-chat-store` (0.3.4, persists a session) and `@State` machines (0.5.0, carry world-state and
HITL across turns), so it deserves to sit **next to RAG as a real "where next" beat in M7**, not be
lumped into a one-line Q&A deflection. Distinguish the two horizons explicitly: RAG shrinks the
*within-a-call* window; memory extends the agent *across* calls.

**Extension:** ✅ **adopted** — new M6 reframe slide ("the window is the budget", now M6.2, decks
renumbered to 11 slides) in `deck-m6.md` + `MASTER-DECK-SPEC.md` + a lead-in beat in
`notes/m6-model-routing.md`; an M0-notes seed sentence connecting context-engineering-the-discipline
to window-as-budget; and a cross-turn-memory horizon bullet on the M7 "Where this is going" slide
(`deck-m7.md` slide 8 / `M7.8`) with the Q&A upgraded from deflection to a named two-horizon answer
in `notes/m7-wrap.md`. No lab or branch change — the branch contract is untouched.

---

## Proposed upgraded day plan (deltas only) — adoption status as of 2026-07-08

| Where | Change | Status |
|---|---|---|
| M0 | Codename footnote; streaming opener (optional) | ✅ in `decks/deck-m0.md` (streaming opener left optional) |
| M2 | "One of four planners" slide | ✅ notes + `decks/deck-m2.md` slide 8; Lab 5 going-further |
| M3 | Fifth guardrail shape: content guardrail + lab step | ✅ **implemented on `main`**: `RequestContentGuardRail` (+ test) wired in `extractProfile`; how-to + worksheet updated; `deck-m3.md` slide 4 |
| M4 | Fault taxonomy row 4 + personality demo | ✅ worksheet going-further + `application.yml` comment + `deck-m4.md` slide 4 (retry demo code deliberately not landed — see note below) |
| M6 | Per-call cost lines; observed-cost table in MODEL_ROUTING.md; caching slide | ✅ MODEL_ROUTING.md table added; `deck-m6.md` slide 8 |
| M7 | Ecosystem slide: Hub, Guide MCP into Claude Code, coding-agent | ✅ SETUP §7 + `deck-m7.md` slide 9 |
| New | Stretch Lab 7: ToolishRag over the catalog | ✅ worksheet `labs/lab7-rag.md` (take-home; code sketches, no lab7 branches — branch contract unchanged) |
| New | "Where this is going" wrap slide | ✅ `deck-m7.md` slide 8 |
| New | Risk register | ✅ docs site: `docs/reference/risk-register.md` |
| M4 | `AgenticEventListener` exercise — observability you build | ✅ **implemented on `main`**: `PlanFlightRecorder` (+ test, fires in mocked integration tests); Lab 4 going-further; `deck-m4.md` slide 9 |
| M6 | Gap 9(a): "the window is the budget" reframe slide | ✅ new `deck-m6.md`/`M6.2` (decks now 11 slides) + `notes/m6-model-routing.md` lead-in beat + M0-notes seed |
| M7 | Gap 9(b): cross-turn memory horizon bullet | ✅ `deck-m7.md` slide 8 / `M7.8` + `notes/m7-wrap.md` Q&A upgraded from deflection to named two-horizon answer |
| All | Slide↔exercise reconciliation for audits v2–v4 (keyless run path) | ✅ every demo/lab run block in `slides/` now carries the keyless `plan`-not-`x` path (mock profile) alongside the real-key `x … -p -r`; M4 STUCK-signature wording aligned to `MaxActionsEarlyTerminationPolicy`; M6 demo flagged as needing a real key (no cost lines under the mock) |

Remaining deliberate non-adoptions: the M4 retry *demo code* (a throwing `researchSessions`
variant) stays a worksheet exercise rather than landed code — landing it would mean rippling the
lab4 branch progression; the Lab 7 implementation likewise stays a sketch until it earns branch
surgery.

**Residual found during the slide-reconciliation pass (2026-07-08) — worksheet, now FIXED:**
`labs/lab4-explainability.md` Step 2 paired the `mock` profile with `x "…" -p -r`, which contradicts
audit v4's own finding (documented in `docs/how-to/use-mock-mode.md`): `x` fails under the mock
profile (`Text content cannot be empty`, the goal-ranker LLM call goes unanswered), and `-p -r` are
`x`-only. The keyless path is `plan "…"` (which prints the plan with no flags). v4 fixed this
everywhere *except* Lab 4. ✅ **Resolved**: the worksheet existed byte-identically on 8 branches
(`main`/feature branch + `lab3-after`, `lab4-broken`, `lab4-after`, `lab5-{before,after}`,
`lab6-{before,after}`); the fix was applied on the feature branch and cherry-picked (`-x`) onto each
lab branch — one `docs(lab4)` commit per branch, worksheet-only, no code touched. The M4 speaker
notes and deck already show the correct `plan` path.

---

## Maintenance risk register (for the repo, not the slides)

- **1.0.0-RC1 is imminent.** The deprecation purge (PR #1750, 30 June 2026, ~1,100 deletions)
  already landed on `main`. Action: when RC1 tags, re-run all six labs against it on a branch
  before changing the pin. Highest exposure: `embabel-agent-test` is officially **incubating** —
  the `FakeOperationContext` / `EmbabelMockitoIntegrationTest` / `whenCreateObject` idioms every
  lab's tests rely on are the most likely surface to shift.
- **0.5.0 behaviour change worth knowing cold:** `@AchievesGoal` on a void method is now rejected
  at startup (previously silently wrong). Good trivia for M3 Q&A.
- **The 2.0 branch** (Spring Boot 4.0.x, Spring Framework 7/JSpecify, Spring AI 2.0.0-M8 with
  vendor-SDK facades, Jackson 3 `tools.jackson.*`) will eventually invalidate the README's
  "Spring Boot 3.5.x" pin. No action now; plant the "where this is going" slide so attendees
  aren't surprised later.
- **CLAUDE.md gotchas re-verified against 0.5.0:** still correct — no `toolGroups` on `@Action`
  (tool groups go on the prompt runner), `creating(T.class).fromPrompt(...)` remains the current
  typed-output idiom, `withLlmByRole` is one of the three documented model-selection mechanisms
  (auto criteria / by-role / explicit).
- **Unverified items** from the research (treat as leads, not facts): exact contents of the #1750
  removals; any PromptContributor changes (none found — assume stable); forms-specific HITL
  support; A2A beyond "incubating starter."

## Sources

GitHub releases + commits API for `embabel/embabel-agent` · Maven Central metadata
(repo1.maven.org, `<latest>0.5.0</latest>`, 2026-06-21) · Embabel 0.5.0 user guide
(docs.embabel.com) · Configuration Reference wiki (logging personalities) · Spring Boot 4 /
Spring AI 2.0 migration wiki · Rod Johnson: "Embabel: A new agent platform for the JVM",
"AI for your Gen AI: How and Why Embabel Plans", "Building Reliable Agentic Systems, Part I",
"Embabel year-end update" (Dec 2025) · The New Stack interview · Dan Vega's Embabel posts ·
Jettro Coenradie's Agentic RAG walkthrough · embabel org repos (coding-agent, guide,
embabel-agent-examples, java-agent-template, urbot, dice).
