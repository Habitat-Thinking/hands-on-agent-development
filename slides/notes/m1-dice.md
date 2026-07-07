# M1 — DICE: behaviour on domain objects — topic notes & research

> Slot: 09:40–10:40 (15 min frame + demo, 40 min lab, 5 min debrief).
> Branches: `lab1-before` → `lab1-after`. Habit: **Model the domain first.**

## The job of this module

Land one idea: **the prompt is rendered context, not the home of the rule.** A rule that belongs to
a domain concept lives *on the type* (as a `PromptContributor`), travels with the data into every
prompt, and — belt and braces — is also enforced by plain code on the same object. One concept, one
owner, two enforcement surfaces.

## Slide beats

1. **The scattered-prompt anti-pattern.** Show the same "avoid vendor keynotes" sentence
   copy-pasted into three prompt strings, each slightly different. Ask which one is current. This
   is prompt-string drift — the LLM era's version of duplicated business logic.
2. **DICE defined.** Domain In Context Engineering: behaviour on the type that owns the concept.
   Show `AttendeeProfile implements PromptContributor` with `contribution()` — "the record doesn't
   just *store* the avoid-list; it knows how to *speak* about it."
3. **Belt and braces.** `contribution()` *asks* the model; the deterministic filter over
   `tags ∩ avoidTopics` *guarantees*. Neither alone is the design — the point is both surfaces hang
   off one owner. (This foreshadows Lab 3: asking vs. enforcing.)
4. **The id-only idiom.** LLM actions emit session *ids* + reasoning; plain code resolves ids to
   `Session` objects. Keep the model's surface as narrow as judgement requires — everything
   deterministic stays in deterministic code. (This is also why cheap models can own more steps —
   pays off in Lab 6.)
5. **Keystone table callback** — row 1 highlighted.

## Live demo script

```bash
git checkout lab1-before
# show AttendeeProfile: a passive bag of fields; show the TODO anchors
git checkout lab1-after
./mvnw spring-boot:run
x "I'm a platform engineer into Kubernetes and resilience — but absolutely no vendor keynotes" -p
```

Point at the prompt log: the avoid-list sentence appears in the shortlist prompt *without the
prompt string mentioning it* — the profile carried it in. Then show the belt: the filtered menu
never contained the vendor sessions anyway. Two surfaces, one rule, one home.

## Research & references

- **Context engineering as the successor to prompt engineering.** The term went mainstream in 2025
  (Tobi Lütke's "context engineering" framing, echoed by Karpathy); Anthropic's *"Effective
  context engineering for AI agents"* (2025) formalises it: curate *what enters the window*, don't
  wordsmith one string. DICE is context engineering with a Java accent: the domain model *is* the
  curation mechanism.
- **DDD lineage.** Eric Evans, *Domain-Driven Design* (2003): ubiquitous language, behaviour-rich
  value objects vs. anemic data bags. `AttendeeProfile` before Lab 1 is Fowler's **Anemic Domain
  Model** anti-pattern (Fowler, 2003); `implements PromptContributor` is the cure, extended to a
  new consumer of domain behaviour — the prompt. For Java crowds this is a one-slide bridge:
  "you already believe this about business logic; the prompt is just another client."
- **Single source of truth / DRY.** Hunt & Thomas, *The Pragmatic Programmer* (1999): every piece
  of knowledge has one authoritative home. Prompt-string drift is a DRY violation you can't grep
  for reliably — the copies are natural-language paraphrases, not identical tokens. That's *worse*
  than code duplication, and worth saying out loud.
- **Structured output as grounding.** The `creating(T.class).fromPrompt(...)` idiom rides the
  provider-level structured-output/function-calling capability (OpenAI structured outputs, 2024;
  Anthropic tool-use). Slide-worthy: the *type* is the contract; malformed output is a retryable
  framework concern, not application code.
- **Defence in depth.** The belt-and-braces pattern maps to OWASP's *LLM Top 10* guidance
  (LLM01 prompt injection): never rely on instructions alone; validate model output in code.
  The deterministic filter means even a prompt-injected "ignore the avoid list" cannot produce a
  vendor keynote in the schedule.

## Track B foil — what to predict out loud before the lab

Tell the room what the ungoverned agent will probably do, so they can score it: invent an untyped
`Map<String,Object>`, put the avoid rule in one prompt string (not on the type), and skip the test.
The gap between that and the worksheet *is* the lesson — write the predictions on a flip chart and
revisit at the debrief.

## Debrief prompts

- Who found the avoid-list in the prompt log? Who *only* added the field and saw nothing change?
  (That's the acceptance check earning its keep — wiring, not fields, is the feature.)
- Where else in your production systems does a rule live in N prompt strings today?

## Going-further threads (for fast finishers)

- `@Tool boolean shouldAvoid(String tag)` on the profile — let the model *ask* instead of being
  told; compare prompt logs (tool-call vs. contributed sentence).
- Precedence when `interests` and `avoidTopics` collide — decide it on the domain object, write a
  test that pins it.

## Transition out

"The rule now has a home. But who decided the *order* the actions run in? Nobody — and that's the
next module."
