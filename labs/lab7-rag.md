# Lab 7 (stretch) — Agentic RAG: retrieve, don't stuff

> **Goal:** shortlist against a 500-session catalog without putting the whole catalog in the prompt.
> **Objective:** retrieval is an *action-level* design decision — the model searches a store
> instead of reading a menu.
> **Habit:** composes habits 1, 2 and 5 — typed domain, derived plan, right-sized spend.
> **Branch:** work on `main` (this is a take-home / follow-up lab; there are no
> `lab7-before/after` branches — the workshop branch contract stays `lab1..lab6`).
> **Status:** stretch lab. There is no `-before`/`-after` branch; you build on `main`. The concrete
> recipe below — the dependency, a **keyless** store, the `ToolishRag` construction, and how to
> attach it — was **compile- and runtime-verified against Embabel 0.5.0**: a keyless
> ingest → store → retrieve round-trip returns the right session id (only the model *deciding* to
> call the search tool needs a key). What stays a *sketch* is the tuning
> (chunking, which `Retrievable` types you search, filters, reranking), which you adapt against the
> RAG section of the [0.5.0 user guide](https://docs.embabel.com/embabel-agent/guide/0.5.0/). It is
> not a drop-in like Labs 1–6.

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

1. **Add the RAG module.** One dependency, pinned like every other (it is *not* on the classpath
   today, so the add is genuinely required):
   ```xml
   <dependency>
     <groupId>com.embabel.agent</groupId>
     <artifactId>embabel-agent-rag-core</artifactId>
     <version>${embabel-agent.version}</version>
   </dependency>
   ```
   It ships `ToolishRag` plus ready `SearchOperations` backends. **Pick the keyless one:**
   `DirectoryTextSearch` does in-JVM text/regex search over a directory and needs **no embeddings**,
   so the build stays green with no keys (rag-core pulls no Lucene — it is self-contained). The
   vector path (`SpringVectorStoreVectorSearch`, or any `SearchOperationsBuilder.withEmbeddingService(...)`)
   needs a real `EmbeddingService` — a provider key or a local model — which **breaks the repo's
   keyless property**. Start with the text store; move to vectors only once you've wired an embedding
   model.
2. **Ingest the catalog into the search directory.** At startup, write each `Session` as a small
   text file **named by its id** (`PC-01.txt`, contents = title + abstract + tags) into a directory,
   then point the store at it with `new DirectoryTextSearch(dir)`. Naming the file by the session id
   is what keeps the id-only idiom alive: retrieval hands back the chunk id, which is the **file name**
   (`PC-01.txt`) — strip the extension to get the session id, then resolve to `Session` via
   `CatalogService`, exactly as `resolve(...)` does today.
3. **Give shortlisting search instead of a menu.** Attach the tool with **`.withReference(...)`** — a
   `ToolishRag` is an `LlmReference`, *not* a tool group, so do **not** reach for `withToolGroup`. The
   0.5.0 Java constructor is wide (no builder yet); this minimal shape compiles:
   ```java
   var rag = new ToolishRag(
       "catalogSearch", "Search the conference catalog",
       new DirectoryTextSearch(dir), ToolishRag.Companion.getDEFAULT_GOAL(),
       SimpleRetrievableResultsFormatter.INSTANCE,
       List.of(),                // vectorSearchFor — none, this is a text store
       List.of(Chunk.class),     // textSearchFor — the Retrievable type it returns (empty list = no hits)
       List.of(),                // hints
       event -> { },             // ResultsListener
       null, null,               // metadata / entity filters — none is fine at runtime
       5000, "");                // maxZoomOutChars, childToolUsageNotes

   // in ConfPlanningCapabilities.shortlist, replace the rendered menu:
   ai.withLlmByRole("cheapest").withReference(rag)
       .withPromptContributor(profile)
       .creating(Shortlisting.class)
       .fromPrompt("Search the catalog for sessions matching the attendee's interests; return the ids you chose.");
   ```
   Runtime notes (all confirmed keyless): `DirectoryTextSearch` does **BM25 text search** — no
   embeddings — and `ToolishRag` exposes two tools the model calls, `<name>_textSearch` and
   `<name>_regexSearch` (parameters `query`, `topK`, `threshold`); a `textSearch` returns lines like
   `chunkId: PC-01.txt 1.00 - …`. Do **not** call `ToolishRag.withEagerSearchAbout(...)` with the text
   store — it throws *"Eager search requires VectorSearch"* (that convenience path needs embeddings);
   here retrieval happens when the model invokes the tool. (`Chunk` is
   `com.embabel.agent.rag.model.Chunk`.)
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
