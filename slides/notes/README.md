# Slide-deck topic notes

One file per workshop phase, expanding `slides/OUTLINE.md` into build-ready material. The
slide-by-slide build specs for Claude Design (layouts, visuals, verbatim on-slide copy, shared
design system) live one level up in [`../decks/`](../decks/README.md) — these notes are the
companion speaker/research layer. Each file
carries the same five sections: **job of the module** (what must land), **slide beats** (ordered,
with the argument each slide makes), **live demo script** (branch + commands + what to point at),
**research & references** (citable sources for slide credibility), and **debrief/Q&A prep**
(including the Track B predictions to make out loud *before* each lab).

| File | Phase | Slot (suggested) | Branches |
|---|---|---|---|
| [m0-orientation.md](m0-orientation.md) | Orientation | 09:00–09:40 | `main` |
| [m1-dice.md](m1-dice.md) | DICE / Lab 1 | 09:40–10:40 | `lab1-before/after` |
| [m2-goap.md](m2-goap.md) | GOAP / Lab 2 | 10:50–11:50 | `lab2-before/after` |
| [m3-guardrails.md](m3-guardrails.md) | Guardrails / Lab 3 | 11:50–12:50 | `lab3-before/after` |
| — lunch — | | 12:50–13:50 | |
| [m4-explainability.md](m4-explainability.md) | Explainability / Lab 4 | 13:50–14:50 | `lab4-broken` → `lab4-after` |
| [m5-extend.md](m5-extend.md) | Extend / Lab 5 (capstone) | 15:00–16:10 | `lab5-before/after` |
| [m6-model-routing.md](m6-model-routing.md) | Model routing / Lab 6 | 16:20–17:10 | `lab6-before/after` |
| [m7-wrap.md](m7-wrap.md) | Wrap: govern the loop | 17:10–17:40 | `main` |
| [gaps-and-extensions.md](gaps-and-extensions.md) | Workshop vs. Embabel state of play (July 2026) | — | — |

Recurring devices the decks should keep consistent:

- **The keystone table** (objective → mechanism → habit) reappears at the end of every module with
  the current row highlighted.
- **Track B predictions**: before each lab, say out loud what an ungoverned agent will get wrong;
  score it at the debrief.
- **The ritual** closes every module: read the planning log, read the trace, confirm the
  acceptance check.
- **Transitions** are written at the foot of each file — each module's last line is the next
  module's problem.

`gaps-and-extensions.md` also holds the maintenance risk register (1.0.0-RC1 / 2.0 horizon);
re-check it before each delivery of the workshop.

Source materials for the M0/M7 framing (habitat engineering, the two ladders, the Habitat
Build Gap, the amplifier thesis) live in the repo's [`reference/`](../../reference/) folder:
`thesovereignengineer.pdf`, `agentic-maturity-model.md`, `cognitive-ladder.md`. Slide copy
quotes these verbatim — check wording against them before editing those slides.
