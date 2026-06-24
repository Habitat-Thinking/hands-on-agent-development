# About domain-in-context engineering

There is a recurring temptation when you build with language models: to treat the prompt as the
place where everything lives. The attendee's preferences, the business rules, the little caveats —
all of it ends up hand-written into prompt strings, copy-pasted from one call site to the next.
ConfPlanner is built the other way around, and this page is about *why*.

**DICE** stands for **D**omain **I**n **C**ontext **E**ngineering. The idea is small and has large
consequences: the behaviour that belongs to a domain concept should live *on the type that
represents that concept*, and travel with the data wherever the data goes — including into the
prompt. The prompt is rendered context, not the home of the rule.

## The example: an attendee who hates vendor keynotes

Consider the rule "this attendee asked to avoid vendor keynotes." Where should it live?

The scattered-prompt answer is to write, in every prompt that touches sessions, a sentence like
"don't recommend anything tagged 'vendor'." That sentence now exists in the shortlist prompt, and
again in the assemble prompt, and again wherever a future feature reads sessions. Each copy can
drift. Each new call site is a place to forget. The rule has no single home, so nothing owns it.

The DICE answer is that the avoid-list is a property of the *attendee*, so it lives on
`AttendeeProfile` — which in ConfPlanner is a Java record that *implements* `PromptContributor`:

```java
public record AttendeeProfile(
        List<String> interests, String role, String experienceLevel,
        List<String> goals, List<String> avoidTopics
) implements PromptContributor {

    @Override
    public String contribution() {
        if (avoidTopics == null || avoidTopics.isEmpty()) return "";
        return "The attendee wants to AVOID these topics: " + String.join(", ", avoidTopics)
                + ". Never recommend a session tagged with any of them.";
    }
}
```

The record does not merely *store* the avoid-list; it knows how to *speak* about it. When the
profile is attached to a prompt runner with `ai.…withPromptContributor(profile)`, its
`contribution()` rides along automatically. The rule is written once, on the thing it belongs to,
and every prompt the profile reaches inherits it for free.

## Why this is the better design

**The rule cannot drift, because it has exactly one home.** Add a new prompt that uses the profile
and the avoid-instruction comes with it. There is no second copy to keep in sync, because there is
no second copy.

**The type system becomes your map of the domain.** When behaviour lives on records, reading the
records tells you what the system knows and what it promises. ConfPlanner takes this seriously: all
domain types are immutable Java records, and the project's conventions forbid passing untyped
`Map`/`Object` between actions. A bag of strings is opaque; a typed record with a `contribution()`
is self-describing.

**The model's job stays small.** A related decision in ConfPlanner is that the LLM actions emit only
session *ids* plus reasoning, and plain code resolves those ids back to `Session` objects from the
catalog. The model is asked to do the part that needs judgement and nothing more; the deterministic
parts stay in deterministic code. DICE and this id-only idiom are the same instinct: keep the
model's surface narrow, and let typed domain code carry the rest.

## The belt-and-braces nuance

`contribution()` *asks* the model to honour the avoid-list — but a model can slip. So ConfPlanner
also filters avoided sessions out of the menu in plain code (`profile.shouldAvoid(session)`) before
the prompt is ever built. The "belt" is the deterministic filter; the "braces" is the
`PromptContributor` instruction. This is worth dwelling on: DICE does not mean "trust the prompt." It
means the *rule* lives on the domain object, and that single home can express itself both as
rendered context (the braces) and as ordinary code the same object exposes (the belt, via
`shouldAvoid`). One concept, one owner, two enforcement surfaces.

## Where it connects

DICE is the runtime face of the first habit, **model the domain first** (see
[the eight habits](the-eight-habits.md)). Its build-time twin is writing your conventions and types
down before code — the same instinct, one altitude up, described in
[the dual harness](the-dual-harness.md). And it sets up everything that follows: because behaviour
is on typed records, [goal-oriented planning](goap.md) can route by type, and
[guardrails](guardrails-and-invariants.md) can be conditions over those types.

When you want to *do* this rather than read about it, Lab 1 walks you through giving
`AttendeeProfile` its voice step by step — see the [tutorials](../tutorials/index.md). For the exact
`PromptContributor` signature and the domain record shapes, see the
[reference](../reference/index.md).
