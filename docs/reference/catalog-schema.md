# Catalog schema

The conference catalog is bundled at `src/main/resources/catalog/uberconf-sample-catalog.json` and
loaded once by `CatalogService` into a [`SessionCatalog`](domain-model.md#sessioncatalog). The data
is **synthetic and fictional** — no real UberConf schedule or real abstracts. The catalog contains
deliberate slot overlaps so the `noDoubleBooking` invariant (see
[actions, conditions, and goals](actions-conditions-goals.md)) has clashes to catch.

## Top-level shape

```json
{
  "sessions": [ /* Session objects */ ],
  "speakers": [ /* Speaker objects */ ]
}
```

| Key | JSON type | Maps to |
|---|---|---|
| `sessions` | array | `List<Session>` |
| `speakers` | array | `List<Speaker>` |

## Session object

| Field | JSON type | Example |
|---|---|---|
| `id` | string | `"PC-01"` |
| `title` | string | `"Kubernetes Without the Tears"` |
| `abstractText` | string | `"Kubernetes Without the Tears: a calm tour of the failure modes…"` |
| `speakers` | array of string | `["Dr. Priya Venkatasubramanian"]` |
| `track` | string | `"Platform & Cloud"` |
| `room` | string | `"Cedar"` |
| `day` | string (ISO date) | `"2026-09-15"` |
| `startTime` | string | `"09:00"` |
| `endTime` | string | `"10:15"` |
| `level` | string | `"Intermediate"` |
| `tags` | array of string | `["kubernetes", "platform-engineering", "operations"]` |

The slot key used for clash detection is `day + " " + startTime` (e.g. `2026-09-15 09:00`).

## Speaker object

| Field | JSON type | Example |
|---|---|---|
| `name` | string | `"Dr. Priya Venkatasubramanian"` |
| `bio` | string | `"Distributed systems researcher turned platform lead…"` |
| `topics` | array of string | `["kubernetes", "consensus", "platform-engineering"]` |

## Counts

| Property | Value |
|---|---|
| Sessions | 35 |
| Speakers | 12 |
| Tracks | 4 — `AI & Data`, `Architecture & DevEx`, `Platform & Cloud`, `Security & Resilience` |
| Days | 3 — `2026-09-15`, `2026-09-16`, `2026-09-17` |
| Slots with more than one session (overlaps) | 9 |
