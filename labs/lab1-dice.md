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
- `extractAttendeeProfile` and `shortlistSessions` carry `// TODO (Lab 1)` anchors.
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
4. **Honour it.** In `shortlistSessions`, attach the profile so its contribution reaches the model
   (`ai.withDefaultLlm().withPromptContributor(profile).creating(...)`), **and** belt-and-braces,
   filter the `menu` to drop any session whose `tags` intersect `avoidTopics`.
5. Build: `./mvnw -q verify`.

## Acceptance check (framework-enforced)

- For a request like *"…but no vendor keynotes"*, sessions tagged with the avoided topic are
  **absent** from the schedule.
- Run with `x "..." -p` and confirm the avoid-list appears in the prompt log — proof the
  `PromptContributor` is wired in, not just the field added.
- A unit test (see `lab1-after`) asserts the filtered shortlist excludes avoided tags.

## Three-track notes

- **Track A (by hand):** make the four edits in your IDE, run `x "..." -p`, read the prompt.
- **Track B (ungoverned agent):** ask Claude Code "add an avoid-topics feature." Watch whether it
  invents an untyped `Map`, skips the `PromptContributor`, or forgets the test. That gap is the lesson.
- **Track C (harness):** orchestrator → `java-implementer` makes the edit under
  `src/main/java/**`; the constraint "actions return typed records, behaviour lives on the domain"
  is what keeps it honest. Two-stage review before it lands.

## Going further

- Add `@Tool boolean shouldAvoid(String tag)` to `AttendeeProfile` and let the model call it as a
  tool instead of reading a sentence. Compare the prompt logs.
- What happens if `avoidTopics` and `interests` conflict (same tag in both)? Decide the precedence
  and encode it on the domain object, not in the prompt.
