# Deck plans — ready for Claude Design

**The handover artifact is [`MASTER-DECK-SPEC.md`](MASTER-DECK-SPEC.md)** — a single
self-contained spec covering all eight decks (82 slides): the shared design system, explicit
graphics directives (diagram-first — every slide leads with a drawable depiction of its
mechanism; copy captions the graphic), and per-slide Layout / Graphic / Copy / Cue. Hand that one
file to Claude Design whole.

The per-deck files below are the working sources the master was assembled from — keep editing
here deck-by-deck, then re-fold changes into the master before a handover. Each file gives, per
slide: **Layout** (structural hint), **Visual** (what to draw), **On-slide copy** (verbatim), and
**Notes** (one-line speaker cue; full speaker notes live in `../notes/m*.md`).

| Deck | File | Shown | Slides |
|---|---|---|---|
| M0 Orientation | [deck-m0.md](deck-m0.md) | 09:00, before Lab 1 | 14 |
| M1 DICE | [deck-m1.md](deck-m1.md) | before Lab 1 hands-on | 9 |
| M2 GOAP | [deck-m2.md](deck-m2.md) | before Lab 2 | 10 |
| M3 Guardrails | [deck-m3.md](deck-m3.md) | before Lab 3 | 11 |
| M4 Explainability | [deck-m4.md](deck-m4.md) | after lunch, before Lab 4 | 11 |
| M5 Extend | [deck-m5.md](deck-m5.md) | before Lab 5 | 10 |
| M6 Model routing | [deck-m6.md](deck-m6.md) | before Lab 6 | 10 |
| M7 Wrap | [deck-m7.md](deck-m7.md) | 17:10, close | 10 |

## Design system (paste this into Claude Design first)

**Audience & tone.** Senior Java/JVM engineers at a full-day conference workshop. Confident,
spare, a little wry. No stock imagery, no robots, no glowing brains. Dark background preferred
(rooms are bright; code is dark).

**Typography.** One strong grotesque for headlines (e.g. Inter/Söhne class), a true monospace for
all code, log lines, and type names (e.g. JetBrains Mono class). Type names like
`AttendeeProfile` are ALWAYS monospace, even in headlines.

**Color semantics (keep exact roles, hues can flex):**
- **Runtime harness (Embabel)** — electric blue.
- **Build-time harness (ai-literacy-superpowers)** — warm amber.
- The two-harness pairing is THE visual motif: whenever both appear, blue left / amber right.
- **TRUE / satisfied condition** — green. **FALSE / unmet condition** — red. Used in world-state
  and plan diagrams; never used decoratively.
- Neutral slate for everything else; one near-white for headline text.

**Recurring components (design once, reuse across all decks):**
1. **Keystone table** — the 6-row objective → mechanism → habit table. Appears at the end of every
   deck with the current row highlighted; rows not yet taught are dimmed.
2. **Habit badge** — small numbered chip ("Habit 3 — Make the contract explicit") top-right of
   every content slide in M1–M6.
3. **Log panel** — a styled terminal block for planning-log excerpts: monospace, dark, with
   TRUE/FALSE tokens colored. Must look like a terminal, not a screenshot.
4. **Plan ribbon** — the pipeline `extract → loadCatalog → shortlist → research → assemble →
   confirm` as a horizontal chain of typed nodes; nodes light up per module.
5. **Ritual footer** — closing slide of every deck: "Read the planning log · Read the trace ·
   Confirm the acceptance check."
6. **Track chips** — A / B / C markers (A = by hand, B = ungoverned agent, C = harness) for the
   three-track slides; C carries the amber harness color.

**Diagrams over screenshots** everywhere except where a slide explicitly says "screenshot".
Diagrams are flat, high-contrast, few words. Arrows mean data flow (produces/consumes), never
vague "relates to".

**Slide furniture.** Module code (M0–M7) bottom-left; slide number bottom-right; no logos except
the title slides. Citations render as a single muted line at the bottom of the slide.
