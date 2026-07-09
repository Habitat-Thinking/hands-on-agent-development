# Quiz — M1: DICE (Lab 1)

> Section: **DICE — behaviour on domain objects**. Habit: **Model the domain first.**
> Four short questions. Attempt them before opening the answers.

## Questions

1. Where should the rule "this attendee avoids vendor keynotes" live, and what specifically goes
   wrong if it lives copy-pasted in three prompt strings instead?
2. You add the `avoidTopics` field and `./mvnw verify` is green. Have you implemented the feature?
   What is the actual proof that it works — and why can't a green build alone show it?
3. `AttendeeProfile` both *contributes a sentence* to the prompt and *filters the shortlist in code*.
   What does each of those do, and why keep **both**?
4. Transfer: name one place in a system you work on where a business rule currently lives in N prompt
   strings (or N copied validations). What would giving it one home look like?

<details>
<summary>Answers &amp; discussion</summary>

1. **On `AttendeeProfile`, as a `PromptContributor`** — so the rule travels with the data into every
   prompt automatically. Copy-pasted into prompt strings you get *prompt-string drift*: a DRY
   violation you can't reliably grep for, because the copies are natural-language paraphrases, not
   identical tokens.
2. **Not yet — the field isn't the feature, the *wiring* is.** Proof: the avoid-list sentence
   appearing in the prompt log on a live run, and/or the keyless filter test
   (`avoidedTopicsAreExcludedFromTheShortlistMenu`). A green build alone can't show the wiring because
   `FakeOperationContext` does not render prompt contributors into its capture.
3. The **contribution()** sentence *asks* the model to avoid the topics; the deterministic **filter**
   over `tags ∩ avoidTopics` *guarantees* it. Defence in depth — even a prompt-injected "ignore the
   avoid list" cannot put a vendor session in the schedule, because the filter is plain code.
4. *(Your answer.)* Good ones name a rule duplicated across prompts/validations and describe moving it
   onto the type/entity that owns the concept, with a test that pins it.

</details>
