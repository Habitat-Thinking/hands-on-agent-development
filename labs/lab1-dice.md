# Lab 1 — DICE: behaviour on domain objects

> **Goal:** the planner refuses to put topics the attendee asked to avoid into their schedule.
> **Objective:** model the domain first — put logic *on* the domain object, not scattered in prompts.
> **Habit:** **Model the domain first.**
> **Ladder rung:** Track A → B (Dictating → Commanding).
> **Branches:** `lab1-before` → `lab1-after`.

## Why this lab

DICE — **D**omain **I**n **C**ontext **E**ngineering — says the place to encode "this attendee
hates vendor keynotes" is on `AttendeeProfile`, where it travels with the data into every prompt,
not copy-pasted into each prompt string. In the baseline, `AttendeeProfile` is a passive bag of
fields. We give it a voice: it becomes a `PromptContributor` and starts contributing its own
avoid-list to the prompt.

## Before state (`lab1-before`)

- `AttendeeProfile` has `interests`, `role`, `experienceLevel`, `goals`. No notion of topics to avoid.
- Three `// TODO (Lab 1)` anchors: in `AttendeeProfile` (the new record component), and in
  `extractAttendeeProfile` and `shortlistSessions`.
- `shortlistSessions` matches on interests only.

## Steps

1. **Add the field.** In `AttendeeProfile`, add `List<String> avoidTopics` (last component).
2. **Give the domain object a voice.** Make `AttendeeProfile implements PromptContributor`
   (`com.embabel.common.ai.prompt.PromptContributor`) and implement
   `String contribution()` returning something like:
   ```java
   @Override
   public String contribution() {
       return avoidTopics == null || avoidTopics.isEmpty()
           ? ""
           : "The attendee wants to AVOID these topics: " + String.join(", ", avoidTopics)
             + ". Never recommend a session tagged with them.";
   }
   ```
3. **Populate it.** Update the `extractAttendeeProfile` prompt to also extract `avoidTopics`.
4. **Honour it — with a domain method, not an inline filter.** Add
   `boolean shouldAvoid(Session session)` to `AttendeeProfile` (behaviour on the domain object is the
   whole DICE point), then in `shortlistSessions` attach the profile so its contribution reaches the
   model (`ai.withDefaultLlm().withPromptContributor(profile).creating(...)`) **and**, belt-and-braces,
   drop any session where `profile.shouldAvoid(session)`.
5. **Update the tests broken by the new field** (see the note below), then build: `./mvnw -q verify`.

> **Your diff will also show…** adding `avoidTopics` as a fifth record component changes the
> `AttendeeProfile` constructor arity, so every `new AttendeeProfile(...)` in the tests stops
> compiling:
>
> ```
> constructor AttendeeProfile in record AttendeeProfile cannot be applied to given types;
>   required: List<String>,String,String,List<String>,List<String>
> ```
>
> Add `List.of()` (an empty avoid-list) as the fifth argument in `ConfPlannerAgentTest` and
> `ConfPlannerAgentIntegrationTest`. The `lab1-after` reference also adds two new unit tests —
> `avoidedTopicsAreExcludedFromTheShortlistMenu` and `shouldAvoidIsCaseInsensitiveOnTags` — that
> prove the filter and the domain method. Writing them is optional but they are the key-free
> acceptance proof (see below).

## Acceptance check (framework-enforced)

- **Keyless (no API key needed):** `./mvnw -q verify` is green, and the `lab1-after` unit test
  `avoidedTopicsAreExcludedFromTheShortlistMenu` asserts the filtered shortlist excludes avoided
  tags. This is the acceptance proof you can run offline — give it equal billing with the live run.
- **Live (needs a key):** for a request like *"…but no vendor keynotes"*, sessions tagged with the
  avoided topic are **absent** from the schedule; run with `x "..." -p` and confirm the avoid-list
  appears in the prompt log — proof the `PromptContributor` is wired in, not just the field added
  (`FakeOperationContext` does not render contributors into its capture, so only the live run shows
  this).

## Three-track notes

- **Track A (by hand):** make the four edits in your IDE, run `x "..." -p`, read the prompt.
- **Track B (ungoverned agent):** ask Claude Code "add an avoid-topics feature." Watch whether it
  invents an untyped `Map`, skips the `PromptContributor`, or forgets the test. That gap is the lesson.
- **Track C (harness):** orchestrator → `java-implementer` makes the edit under
  `src/main/java/**`; the constraint "actions return typed records, behaviour lives on the domain"
  is what keeps it honest. Two-stage review before it lands.

## Going further

- You already wrote `shouldAvoid(Session)` in step 4. Now add a *tool* variant —
  `@Tool boolean shouldAvoid(String tag)` on `AttendeeProfile` — and let the model call it instead
  of reading the contributed sentence. Compare the prompt logs: the sentence pushes the rule in; the
  tool lets the model pull it on demand.
- What happens if `avoidTopics` and `interests` conflict (same tag in both)? Decide the precedence
  and encode it on the domain object, not in the prompt.
