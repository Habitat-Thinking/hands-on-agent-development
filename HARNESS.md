# HARNESS.md

The build-time harness for ConfPlanner: the constraints a change must satisfy before it lands, and
how each is enforced. This is the runtime harness's twin — Embabel guards the *agent*, this guards
the *engineering of the agent*.

## Context

- Stack: Java 21, Embabel `0.5.0`, Spring Boot `3.5.x`, Maven wrapper.
- Domain: a conference planner over a synthetic catalog. See `AGENTS.md` for stack facts and gotchas.
- Source of truth for behaviour: the lab worksheets' acceptance checks.

## Constraints (Embabel-specific)

| # | Constraint | Enforced by |
|---|---|---|
| C1 | Actions return typed domain **records** — no untyped maps between actions | code review + compile |
| C2 | **Never hard-wire flow order.** New capability is added via new types/actions/agents only | review + planning-log check |
| C3 | Every action that mutates state or calls a tool is **guarded** (precondition or secured) | review |
| C4 | `@Condition` methods are **side-effect-free** | review |
| C5 | An **invariant** the goal must hold is a `@Condition` the goal action `pre`-requires (not just a `post`) | review + a test that proves it bites |
| C6 | **LLM choice is justified per action** by return-type complexity; routing via `withLlmByRole` + config | `MODEL_ROUTING.md` review |
| C7 | **No secrets** in the tree; `.env` only via `.env.example`; keys never committed | `.gitignore` + secret scan |
| C8 | The build is **green without API keys** (tests mock the LLM). Only `lab4-broken` may fail at runtime | CI (`./mvnw clean verify`) |
| C9 | Catalog data is **synthetic/fictional** — no real schedule or real abstracts | review |

## Verification

- **Deterministic gate:** `./mvnw clean verify` in CI on every push/PR (`.github/workflows/build.yml`).
- **Agent gate (Track C):** orchestrator → `java-implementer` (scope `src/main/java/**`) →
  two-stage review against C1–C6 before merge.
- **Drift check:** when a lab changes the agent shape, confirm the planning log still reads as the
  worksheet says, and the regression tests for prior labs stay green.

## Reservoir

The human in the loop is the verifier the harness cannot verify. Keep sessions bounded; when you've
been reading planning logs for an hour, stop and re-read the acceptance check before approving.
