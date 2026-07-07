# M6 — Model routing: the cheapest model that passes — topic notes & research

> Slot: 16:20–17:10 (15 min frame + demo, 30 min lab, 5 min debrief).
> Branches: `lab6-before` → `lab6-after`. Habit: **Right-size the model.**

## The job of this module

Reframe model choice from a global default into a **per-action design parameter** with a mechanical
heuristic (route by return-type complexity), a config-not-code mechanism (`withLlmByRole`), and a
reviewable record (`MODEL_ROUTING.md`). The sleeper theme: the same knob answers the data-residency
question — route a step to a *local* model when its data can't leave the building.

## Slide beats

1. **The uneven-spend slide.** Token share of one schedule run (from the explanation doc):
   extract+shortlist ~15%, research ~35%, assemble ~45%, confirm ~0% (plain code). "If everything
   uses your best model, you're paying premium rates to pull five fields out of a sentence."
2. **The opposite of right-sizing isn't 'be cheap' — it's *not deciding*.** One default model
   silently absorbing work it's over-qualified for. Right-sizing makes each step's cost a decision
   you can later defend or reduce.
3. **The heuristic** (make it a poster): *flat list of strings → `cheapest`; carries judgement —
   scores, rationale, a conflict-free arrangement → `best`.* Show the five-action table with roles
   and one-line justifications. The return type is an honest proxy for how much judgement the step
   demands — and the Lab 1 id-only idiom is what keeps cheap steps cheap.
4. **Config, not code.** Code commits to a *role* (`withLlmByRole("cheapest")`); `application.yml`
   binds role → model. Model names churn quarterly; roles don't. Re-routing the estate = a config
   edit; tests mock the LLM so the build stays green with no keys.
5. **The regulated-environment lever.** Point any role at a local model (Ollama tag under a Spring
   profile). The extraction step reads the attendee's raw words — in a regulated setting, route
   exactly that step on-prem, keep the rest in the cloud. Name the trade honestly: latency and
   some quality, in exchange for residency — and *record it* in `MODEL_ROUTING.md`.
6. **Measure before optimising.** Same request, all-`best` vs routed; since 0.4.0 the log prices
   each LLM call, so read the comparison in **dollars per action**, not just tokens, and record
   it in `MODEL_ROUTING.md`'s observed-cost table. "A routing decision you can't see in the log
   is a guess." Bonus lever: prompt caching (0.4.0+, Anthropic) cheapens repeated context like
   the catalog menu — routing picks the model, the budget caps the run, caching cuts the price
   of what's left.
7. **Keystone table callback** — row 6, and note the ladder: this lab runs at Orchestrating.

## Live demo script

```bash
git checkout lab6-after
grep -n "withLlmByRole" -r src/main/java   # four call sites, two roles — the whole diff
./mvnw spring-boot:run
x "…" -p -r     # planning log shows which model served each action + the token/budget lines
```

If time allows, the measurement beat: flip `cheapest: gpt-4.1-nano` to the same model as `best`,
re-run, compare the budget lines live. Close on `MODEL_ROUTING.md`: the table is the *reviewable*
artifact — Track C's constraint is "LLM choice is justified per action by return-type complexity."

## Research & references

- **FrugalGPT.** Chen, Zaharia, Zou (Stanford, 2023): LLM cascades and routing matched GPT-4
  quality on benchmarks at up to ~98% cost reduction. The founding citation for "routing is a
  system-design problem, not a procurement problem."
- **RouteLLM** (LMSYS, 2024): learned routers between strong/weak model pairs cut costs
  substantially at near-equal quality; open-sourced. Also the commercial wave (Martian,
  Not Diamond, OpenRouter; provider-side routers like GPT-5's auto-routing in 2025) — the industry
  has voted that per-request model choice is a first-class layer. Embabel's role indirection is
  the Java-config-shaped version of that layer, with a human-reviewable routing table instead of a
  learned black box — for regulated shops that's a feature, say so.
- **Small-model economics.** Price-per-token spans ~2 orders of magnitude between frontier and
  small models (any current pricing page makes the point — keep the slide relative, not absolute,
  so it doesn't date). Latency follows the same curve, which matters for interactive steps.
- **Data residency / sovereignty.** GDPR + sectoral rules (finance, health, defence) make "which
  model sees this field" a compliance question. The role-binding answer — same code, different
  binding per profile — is the cleanest story in the room for hybrid local/cloud agents. Ollama /
  vLLM / llama.cpp for local serving; note Spring AI's OpenAI-compatible + Ollama support is what
  Embabel inherits.
- **Budget interplay (M3 callback).** `Budget(0.50, 20, 200_000)` is the floor under routing:
  routing optimises spend *within* the cap; the cap catches routing mistakes. Two mechanisms, one
  cost story.
- **Right tool for the return type** is also a *quality* argument, not only cost: constrained
  extraction tasks show negligible quality gaps between tiers, while open-ended synthesis shows
  large ones — which is precisely the heuristic's shape. (Frame as observed pattern; invite
  attendees to verify with the measurement exercise rather than trust a benchmark slide.)

## Track B foil — predict out loud

Ask an ungoverned agent to "make it cheaper" and it will hard-code a cheaper model name into Java —
a rewrite that will be wrong by next quarter — or downgrade *everything*, including assemble.
The harnessed answer moves the choice to config and can justify each role by return type.

## Debrief prompts

- "Which action would you route to a local model first in *your* system, and what field drives
  that?" (Get one concrete answer per table.)
- "What's your org's `MODEL_ROUTING.md` today?" (Usually: a Slack thread and a default constant.)

## Transition out

"Six habits, six mechanisms. Two run through everything — testing the seams and governing the loop.
Let's close the loop on the loop."
