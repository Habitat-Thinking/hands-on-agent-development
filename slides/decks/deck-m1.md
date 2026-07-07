# Deck M1 — DICE: behaviour on domain objects (9 slides)

> Shown before Lab 1 hands-on. Habit badge: "Habit 1 — Model the domain first".
> Speaker notes: `../notes/m1-dice.md`.

## Slide 1 — Module title
- **Layout:** section title.
- **Visual:** plan ribbon with `extract` and `shortlist` nodes lit blue.
- **On-slide copy:**
  - "M1 — DICE: behaviour on domain objects"
  - "The prompt is rendered context, not the home of the rule."

## Slide 2 — The anti-pattern: prompt-string drift
- **Layout:** three stacked code cards, deliberately untidy.
- **Visual:** three prompt strings, each containing a slightly different paraphrase of "don't
  recommend vendor content", differences highlighted; a red "which one is current?" stamp.
- **On-slide copy:**
  - Headline: "The same rule, three homes, three wordings."
  - Sub: "Duplication you can't grep for — the copies are paraphrases, not tokens."
- **Notes:** DRY violation, but worse than code duplication.

## Slide 3 — DICE
- **Layout:** definition + one code panel.
- **Visual:** `AttendeeProfile` record card with a small speaker-icon; `contribution()` method
  glowing.
- **On-slide copy:**
  - Headline: "Domain In Context Engineering — put the rule ON the type that owns it."
  - Code (abbrev): `record AttendeeProfile(...) implements PromptContributor { String
    contribution() { ... "AVOID these topics: ..." } }`
  - Sub: "The record doesn't just store the avoid-list. It knows how to speak about it."
- **Notes:** rule travels with the data into every prompt.

## Slide 4 — You already believe this
- **Layout:** bridge slide, two book-spine cards.
- **Visual:** "Evans, DDD (2003)" and "Fowler, Anemic Domain Model" cards; arrow to the
  `AttendeeProfile` card.
- **On-slide copy:**
  - Headline: "Behaviour-rich domain objects — the prompt is just a new client."
  - "Anemic record = data bag = the anti-pattern you already avoid in business logic"
  - Citation line: "Evans 2003 · Fowler 2003 · Anthropic, 'Effective context engineering' 2025"
- **Notes:** one-slide bridge for Java crowds.

## Slide 5 — Belt and braces
- **Layout:** two-lane diagram.
- **Visual:** one `avoidTopics` source node fanning into two lanes: lane 1 "braces" →
  `contribution()` → prompt (blue, dashed = asks); lane 2 "belt" → `shouldAvoid()` code filter →
  menu (solid = guarantees).
- **On-slide copy:**
  - Headline: "One rule, one owner, two enforcement surfaces."
  - "Braces: `contribution()` asks the model"
  - "Belt: the code filter makes vendor sessions unreachable — even under prompt injection"
- **Notes:** foreshadows Lab 3's asking-vs-enforcing; OWASP LLM01 in notes.

## Slide 6 — Keep the model's job small
- **Layout:** before/after comparison.
- **Visual:** left: model emits full `Session` JSON (heavy, red-tinged); right: model emits
  `["s-104","s-217"]` + reasoning, code resolves ids (light, green-tinged).
- **On-slide copy:**
  - Headline: "The id-only idiom: the model does judgement, code does the rest."
  - Sub: "Narrow surface → fewer hallucinated fields → cheaper models can own more steps
    (see Lab 6)."
- **Notes:** `resolve()` drops invented ids silently — the type system as bouncer.

## Slide 7 — Predict the foil
- **Layout:** checklist card.
- **Visual:** Track B chip; three predictions with empty checkboxes (scored at debrief).
- **On-slide copy:**
  - Headline: "Track B forecast — score the ungoverned agent against this:"
  - "☐ Invents an untyped `Map` instead of a record field"
  - "☐ Puts the rule in one prompt string, not on the type"
  - "☐ Skips the test"
- **Notes:** write predictions before the lab; revisit after.

## Slide 8 — The lab
- **Layout:** task card.
- **Visual:** branch chip `lab1-before → lab1-after`; four numbered steps as compact chips.
- **On-slide copy:**
  - "Add `avoidTopics` → give the profile a voice (`PromptContributor`) → extract it → honour it
    (attach + filter)"
  - Acceptance: "avoided tags absent from the schedule · avoid-list visible in the prompt log
    (`-p`) · filtered-shortlist test green"
  - "Worksheet: `labs/lab1-dice.md` · 40 min"
- **Notes:** the acceptance check is wiring, not fields.

## Slide 9 — Keystone + ritual
- **Layout:** keystone table, row 1 highlighted; ritual footer.
- **On-slide copy:**
  - Row 1 lit: "Typed domain models (DICE) · `PromptContributor` on the domain · Model the
    domain first"
  - Transition line: "The rule has a home. Who decided the ORDER the actions run in? Nobody.
    That's next."
- **Notes:** straight into Lab 1; M2 after debrief.
