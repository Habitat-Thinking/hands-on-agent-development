# Domain model

Every domain type is a Java `record` in package `com.russmiles.confplanner.domain`. All are
immutable. Actions pass these typed records between each other; no untyped `Map`/`Object` crosses an
action boundary. The types also drive planning order — see
[Actions, conditions, and goals](actions-conditions-goals.md).

The tables below list each record's components (the canonical constructor parameters), their types,
and the lab in which the field first appears.

## AttendeeProfile

The structured profile extracted from the attendee's free-text request. Implements
`com.embabel.common.ai.prompt.PromptContributor`.

| Component | Type | Description |
|---|---|---|
| `interests` | `List<String>` | Concrete topics/technologies, as lower-case tags. |
| `role` | `String` | The attendee's job role (stated or inferred). |
| `experienceLevel` | `String` | One of `Beginner`, `Intermediate`, `Advanced`. |
| `goals` | `List<String>` | What the attendee wants from the conference, as short phrases. |
| `avoidTopics` | `List<String>` | Topics/tags the attendee explicitly does not want (Lab 1). |

| Member | Signature | Description |
|---|---|---|
| `contribution()` | `String` | `PromptContributor` method. Returns an avoid-topics sentence, or `""` when `avoidTopics` is null/empty. |
| `shouldAvoid(Session)` | `boolean` | True when the session carries a tag matching any `avoidTopics` entry (case-insensitive). |

## Speaker

A conference speaker. Fictional data loaded from the catalog.

| Component | Type | Description |
|---|---|---|
| `name` | `String` | Speaker name. |
| `bio` | `String` | Short biography. |
| `topics` | `List<String>` | Topics the speaker is known for; used by the networking plan (Lab 5). |

## Session

A single conference session. The `day` + `startTime` pair forms the time slot used for clash
detection.

| Component | Type | Description |
|---|---|---|
| `id` | `String` | Catalog id (e.g. `PC-01`). Used everywhere the model refers to a session. |
| `title` | `String` | Session title. |
| `abstractText` | `String` | The session abstract. |
| `speakers` | `List<String>` | Names of the session's speakers. |
| `track` | `String` | Track name. |
| `room` | `String` | Room name. |
| `day` | `String` | Day, ISO date (e.g. `2026-09-15`). |
| `startTime` | `String` | Start time (e.g. `09:00`). |
| `endTime` | `String` | End time (e.g. `10:15`). |
| `level` | `String` | Audience level (e.g. `Intermediate`). |
| `tags` | `List<String>` | Coarse subject tags for matching and the avoid filter. |

| Member | Signature | Description |
|---|---|---|
| `slot()` | `String` | The clash key: `day + " " + startTime`. A session occupies exactly one slot. |

## SessionCatalog

The whole conference. Loaded once from JSON by `CatalogService`. See
[Catalog schema](catalog-schema.md).

| Component | Type | Description |
|---|---|---|
| `sessions` | `List<Session>` | Every session in the catalog. |
| `speakers` | `List<Speaker>` | Every speaker in the catalog. |

## CandidateSessions

The shortlist: the sessions that plausibly match the attendee. A type distinct from
`SessionCatalog`, produced by `shortlistSessions`.

| Component | Type | Description |
|---|---|---|
| `sessions` | `List<Session>` | The shortlisted sessions. |

## SessionInsight

A researched verdict on one shortlisted session (Lab 2).

| Component | Type | Description |
|---|---|---|
| `session` | `Session` | The session this insight is about. |
| `whyRelevant` | `String` | One-sentence relevance statement. |
| `matchScore` | `double` | 0.0–1.0; higher means a better fit for the attendee's goals. |

## ResearchedSessions

The shortlist annotated with research (Lab 2). Consumed by `assembleSchedule`, which is what makes
the planner route through `researchSessions`.

| Component | Type | Description |
|---|---|---|
| `insights` | `List<SessionInsight>` | One insight per researched session. |

## ScheduleItem

One chosen session placed in one slot.

| Component | Type | Description |
|---|---|---|
| `session` | `Session` | The chosen session. |
| `slot` | `String` | The slot label (day + start time) this session occupies. |

## DraftSchedule

A proposed schedule that has not yet passed the guardrails (Lab 3). Produced by `assembleSchedule`;
gated by the `noDoubleBooking` invariant before `confirmSchedule` consumes it.

| Component | Type | Description |
|---|---|---|
| `items` | `List<ScheduleItem>` | The proposed schedule items. |
| `rationale` | `String` | The reasoning behind the picks. |

## PersonalSchedule

The conflict-free personal schedule — the goal of `ConfPlannerAgent`. Implements
`com.embabel.agent.domain.library.HasContent`.

| Component | Type | Description |
|---|---|---|
| `items` | `List<ScheduleItem>` | The confirmed, clash-free schedule items. |
| `rationale` | `String` | The reasoning behind the schedule. |

| Member | Signature | Description |
|---|---|---|
| `getContent()` | `String` | `HasContent` method. Renders the schedule as Markdown for the shell. |

## NetworkingPlan

Who the attendee should try to meet — the goal of `ConfNetworkingAgent` (Lab 5). Implements
`HasContent`.

| Component | Type | Description |
|---|---|---|
| `peopleToMeet` | `List<String>` | Suggested people to meet. |
| `rationale` | `String` | The reasoning behind the suggestions. |

| Member | Signature | Description |
|---|---|---|
| `getContent()` | `String` | `HasContent` method. Renders the plan as Markdown for the shell. |

## PremiumBriefing

The output of the secured premium tool (Lab 3). Not consumed by the schedule goal, so it never
appears in the schedule plan.

| Component | Type | Description |
|---|---|---|
| `summary` | `String` | A one-paragraph briefing across the researched sessions. |
