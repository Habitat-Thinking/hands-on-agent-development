# Actions, conditions, and goals

The agent's behaviour is defined by typed `@Action` methods, `@Condition` methods, and
`@AchievesGoal` markers. The planner (GOAP) derives execution order from each action's input and
output types and its `pre`/`post` condition contracts. Types are described in the
[domain model](domain-model.md).

!!! note "This reference describes the finished agent (`main`)"
    The components and actions below are the **end-state** after all six labs. Earlier lab branches
    carry a subset — e.g. before Lab 3, `assembleSchedule` *is* the goal and returns `PersonalSchedule`
    (there is no `DraftSchedule` / `confirmSchedule`); `ConfPlanningCapabilities`, `ConfNetworkingAgent`,
    and the `cheapest` / `best` roles arrive in Labs 5–6. For what exists at a given step, follow that
    lab's worksheet.

The following Embabel 0.5.0 facts govern these declarations:

- A `post` on the `@AchievesGoal` action is a planning promise, not a runtime gate. An invariant
  bites only when a goal action lists it in `pre` and a separate producer carries the `post` with
  `canRerun = true`.
- An `@Agent` registers exactly **one** goal type. Two `@AchievesGoal` actions in one agent register
  neither.
- An agent plans **only with its own actions**. It cannot borrow another agent's or an
  `@EmbabelComponent`'s actions; shared logic is held in a plain `@Service` and wrapped per agent.
- `@Action` has no `toolGroups` member. Tool groups are added on the prompt runner.

## Components

| Component | Package | Stereotype | Goal type |
|---|---|---|---|
| `ConfPlannerAgent` | `...confplanner.agent` | `@Agent` | `PersonalSchedule` |
| `ConfNetworkingAgent` | `...confplanner.agent` | `@Agent` | `NetworkingPlan` |
| `ConfPlanningCapabilities` | `...confplanner.agent` | `@Service` | — (no actions; shared logic) |
| `CatalogService` | `...confplanner.service` | `@Service` | — (loads the catalog) |
| `RequestContentGuardRail` | `...confplanner.agent` | `UserInputGuardRail` | — (validates raw user input; attached via `withGuardRails(...)` on `extractProfile`) |
| `PlanFlightRecorder` | `...confplanner.observability` | `@Component` / `AgenticEventListener` | — (records planning cycles, replans, and action runs; emits one `[flight-recorder]` summary line on finish or `STUCK`) |

`@Agent` descriptions:

| Agent | `@Agent(description=...)` |
|---|---|
| `ConfPlannerAgent` | "Produce a conflict-free personal UberConf schedule from a free-text request" |
| `ConfNetworkingAgent` | "Suggest who an attendee should meet at the conference" |

## ConfPlanningCapabilities (`@Service`)

Holds the shared pipeline logic and prompts. Contributes no actions itself; each agent declares thin
`@Action` wrappers that delegate here.

| Method | Inputs | Output | LLM role | Notes |
|---|---|---|---|---|
| `extractProfile` | `UserInput`, `Ai` | `AttendeeProfile` | `cheapest` | Extracts the profile from free text. |
| `catalog` | — | `SessionCatalog` | none | Returns the catalog from `CatalogService`. Deterministic. |
| `shortlist` | `AttendeeProfile`, `SessionCatalog`, `Ai` | `CandidateSessions` | `cheapest` | Attaches the profile as a `PromptContributor`; pre-filters avoided sessions in code. |
| `research` | `CandidateSessions`, `Ai` | `ResearchedSessions` | `best` | One insight + match score per shortlisted id. |

Helper records and methods:

| Member | Kind | Description |
|---|---|---|
| `Shortlisting(List<String> sessionIds, String reasoning)` | record | Model output for shortlisting. |
| `Insight(String sessionId, String whyRelevant, double matchScore)` | record | Per-session model output (id-only). |
| `ResearchOutput(List<Insight> insights)` | record | Model output for research. |
| `menuLine(Session)` | `static String` | Formats a session as `id: title [tags] (track, level)`. |
| `resolve(List<Session>, List<String> ids)` | `static List<Session>` | Resolves model-chosen ids back to catalog sessions, dropping invented ids. |

## ConfPlannerAgent — actions

| Action | Inputs | Output | `pre` | `post` | `canRerun` | LLM role |
|---|---|---|---|---|---|---|
| `extractAttendeeProfile` | `UserInput`, `Ai` | `AttendeeProfile` | — | — | — | `cheapest` (via service) |
| `loadCatalog` | `AttendeeProfile` | `SessionCatalog` | — | — | — | none |
| `shortlistSessions` | `AttendeeProfile`, `SessionCatalog`, `Ai` | `CandidateSessions` | — | `hasCandidates` | — | `cheapest` (via service) |
| `researchSessions` | `CandidateSessions`, `Ai` | `ResearchedSessions` | — | — | — | `best` (via service) |
| `assembleSchedule` | `AttendeeProfile`, `ResearchedSessions`, `Ai` | `DraftSchedule` | `hasCandidates` | `noDoubleBooking` | `true` | `best` |
| `confirmSchedule` | `DraftSchedule` | `PersonalSchedule` | `noDoubleBooking` | — | — | none |
| `premiumBriefing` | `ResearchedSessions`, `Ai` | `PremiumBriefing` | — | — | — | `default` |

Inferred plan: `UserInput → extractAttendeeProfile → loadCatalog → shortlistSessions →
researchSessions → assembleSchedule → confirmSchedule`.

Nested record: `ScheduleDraft(List<String> sessionIds, String rationale)` — the model's output when
assembling.

## ConfPlannerAgent — conditions

| Condition (`name`) | Input | Returns true when | Side-effect-free |
|---|---|---|---|
| `hasCandidates` | `CandidateSessions` | `candidates != null && !candidates.sessions().isEmpty()` | yes |
| `noDoubleBooking` | `DraftSchedule` | all `ScheduleItem.slot()` values are distinct | yes |

## ConfPlannerAgent — goal

| Goal action | `@AchievesGoal(description=...)` | Output | Gated by |
|---|---|---|---|
| `confirmSchedule` | "Produce a conflict-free personal schedule" | `PersonalSchedule` | `pre = {"noDoubleBooking"}` |

## ConfPlannerAgent — secured tool

| Action | Annotation | Authority expression | Output |
|---|---|---|---|
| `premiumBriefing` | `@SecureAgentTool` | `hasAuthority('conf:premium')` | `PremiumBriefing` |

`@SecureAgentTool` is in package `com.embabel.agent.mcpserver.security`. It is enforced when the tool
is exposed over an MCP server with an authenticated caller; without `conf:premium` the call is denied
before any LLM spend. `PremiumBriefing` is not consumed by the schedule goal, so the secured action
never enters the schedule plan. The web-security filter chain needs Spring MVC, so the shell app
excludes `AgentMcpServerAutoConfiguration` (see [configuration](configuration.md)).

## ConfNetworkingAgent — actions

A separate `@Agent` (Lab 5) whose single goal is `NetworkingPlan`. It re-declares the upstream
wrappers because an agent plans only with its own actions; each delegates to
`ConfPlanningCapabilities`.

| Action | Inputs | Output | `pre` | `post` | LLM role |
|---|---|---|---|---|---|
| `extractAttendeeProfile` | `UserInput`, `Ai` | `AttendeeProfile` | — | — | `cheapest` (via service) |
| `loadCatalog` | `AttendeeProfile` | `SessionCatalog` | — | — | none |
| `shortlistSessions` | `AttendeeProfile`, `SessionCatalog`, `Ai` | `CandidateSessions` | — | — | `cheapest` (via service) |
| `researchSessions` | `CandidateSessions`, `Ai` | `ResearchedSessions` | — | — | `best` (via service) |
| `planNetworking` | `AttendeeProfile`, `ResearchedSessions`, `Ai` | `NetworkingPlan` | — | — | `default` |

Inferred plan: `UserInput → extractAttendeeProfile → loadCatalog → shortlistSessions →
researchSessions → planNetworking`.

Nested record: `NetworkingDraft(List<String> peopleToMeet, String rationale)` — the model's output
when planning networking.

## ConfNetworkingAgent — goal

| Goal action | `@AchievesGoal(description=...)` | Output |
|---|---|---|
| `planNetworking` | "Produce a networking plan of people to meet" | `NetworkingPlan` |

## LLM-role routing summary

Each LLM action runs under a role bound to a model in `application.yml`. The authoritative role →
model → action table is the [model routing reference](model-routing.md); in brief:

| Role | Default model | Used by |
|---|---|---|
| `cheapest` | `gpt-4.1-nano` | `extractAttendeeProfile`, `shortlistSessions` |
| `best` | `gpt-4.1` | `researchSessions`, `assembleSchedule` |
| `default` (`withDefaultLlm()`) | `gpt-4.1-mini` | `premiumBriefing`, `planNetworking` |
