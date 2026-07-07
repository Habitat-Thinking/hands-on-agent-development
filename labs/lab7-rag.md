# Lab 7 (stretch) — Agentic RAG: retrieve, don't stuff

> **Goal:** shortlist against a 500-session catalog without putting the whole catalog in the prompt.
> **Objective:** retrieval is an *action-level* design decision — the model searches a store
> instead of reading a menu.
> **Habit:** composes habits 1, 2 and 5 — typed domain, derived plan, right-sized spend.
> **Branch:** work on `main` (this is a take-home / follow-up lab; there are no
> `lab7-before/after` branches — the workshop branch contract stays `lab1..lab6`).
> **Status:** stretch lab. The steps are verified against Embabel 0.5.0's module layout
> (`embabel-agent-rag-core` ships `ToolishRag`); the code blocks are sketches to adapt against
> the [0.5.0 user guide's RAG section](https://docs.embabel.com/embabel-agent/guide/0.5.0/), not
> drop-in solutions like Labs 1–6.

## Why this lab

Every lab so far loads the **whole** catalog and renders it into the shortlist prompt as a menu.
At ~30 synthetic sessions that is the right call — simple, legible, cheap enough. The moment
someone asks *"what about 500 sessions?"* the menu becomes the problem: most of your token spend
(see `MODEL_ROUTING.md`) is catalog lines the model ignores.

Embabel's answer is **`ToolishRag`** (module `embabel-agent-rag-core`): expose search over a store
as *tools the model calls*, so the model retrieves the handful of sessions it actually needs.
The prompt shrinks from "here is everything" to "here is how to look" — which retroactively pays
off Lab 6: the cheap model's job gets smaller again.

## Steps (sketch)

1. **Add the RAG modules.** `embabel-agent-rag-core` (and the ingestion pipeline /
   Lucene-backed store per the 0.5.0 guide — artifact names are in the guide's module directory).
2. **Ingest the catalog.** At startup, index each `Session`'s title, abstract and tags into the
   store, keyed by session id. Keep the id-only idiom: the store returns ids + snippets; code
   resolves ids to `Session` via `CatalogService`, exactly as `resolve(...)` does today.
3. **Give shortlisting search instead of a menu.** In `ConfPlanningCapabilities.shortlist`,
   replace the rendered menu with `ToolishRag` search tools on the prompt runner, and reword the
   prompt: *"search the catalog for sessions matching the attendee's interests; return the ids
   you chose."*
4. **Keep the belt.** The avoid-topics filter from Lab 1 must still hold: filter retrieved ids
   through `profile.shouldAvoid(...)` in code before building `CandidateSessions`. Retrieval
   changes where candidates come from, never what the guardrails allow.
5. **Measure.** Same request, menu vs RAG: compare prompt size and the per-call cost lines in the
   planning log. Record both in `MODEL_ROUTING.md`'s observed-cost table.

## Acceptance check

- The shortlist prompt no longer contains the full catalog; the planning log shows retrieval tool
  calls instead.
- The Lab 1 acceptance check still passes: avoided topics never reach the schedule (belt intact).
- All existing tests stay green — retrieval changed an action's *implementation*, so the plan
  (`extract → loadCatalog → shortlist → research → assemble → confirm`) is unchanged. That is
  habit 6 again: you swapped how a step works without rewiring anything.

## Going further

- Persist multi-turn context with `embabel-chat-store` (0.3.4+) so a returning attendee's
  preferences survive across shell sessions.
- Try a second backend (Neo4j / pgvector are incubating at 0.5.0) behind the same action —
  storage is now a binding decision, like models in Lab 6.
- Read Jettro Coenradie's *"Agentic RAG with Embabel — a complete walkthrough"* for a fuller
  treatment.
