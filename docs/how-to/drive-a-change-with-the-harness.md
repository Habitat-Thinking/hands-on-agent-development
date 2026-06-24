# Drive a change with the build-time harness

You want to make a ConfPlanner change through Track C — the governed path — instead of editing Java
by hand. The orchestrator plans the change, the `java-implementer` agent makes the edit under
`src/main/java/**`, and the constraint-gate plus two-stage review gate it before it lands. You
supervise: read the plan, gate the review, confirm the acceptance check.

## 1. State the goal and the acceptance check

Give the orchestrator the intent, not the steps — and the acceptance check the framework will
confirm. For example:

> Add a research step between shortlist and assemble. Acceptance: the planning log reads
> `… → shortlist → research → assemble` with no flow rewiring; the `PersonalSchedule` integration
> test still reaches its goal.

## 2. Read the plan before approving

The orchestrator returns a plan that dispatches `java-implementer` with a scoped task. Check that the
scope is `src/main/java/**` (and `src/test/java/**`), that it does not touch `pom.xml`,
`application.yml`, or governance files unless the task explicitly asks, and that the task names the
acceptance check.

## 3. Let the implementer make the edit and verify

`java-implementer` reads `CLAUDE.md` and `AGENTS.md` (the GOTCHAS are load-bearing), makes the
smallest change that satisfies the task, writes or updates a test that proves the acceptance check
(mocking the LLM — never requiring a key), and runs:

```bash
./mvnw -q verify
```

It returns the files changed, the verification result, and whether the acceptance check is met. It
does **not** commit.

## 4. Gate the two-stage review

Review the diff against the constraints in [`HARNESS.md`](https://github.com/Habitat-Thinking/hands-on-agent-development/blob/main/HARNESS.md)
(C1–C9 — typed records, no hard-wired order, guarded actions, side-effect-free conditions,
goal-gated invariants, justified model choice, and so on). The diff must satisfy every constraint
that applies before it can land.

## 5. Confirm the acceptance check, then land

Run the ritual: read the planning log, read the trace, confirm the worksheet's acceptance check. If a
lab changed the agent shape, confirm prior labs' regression tests stay green. Only then land the
change.

## If the change needs a dependency or config edit

`java-implementer` will stop and report the need rather than editing `pom.xml` or `application.yml`
itself. Make that edit deliberately (e.g. follow [Route models by role](route-models-by-role.md) for
`application.yml`), then re-dispatch the Java change.

---

For the operational ladder (Dictating → … → Supervising) and *why* Track C builds the Embabel agent
using the disciplines the agent itself embodies, see
[About the dual harness](../explanation/the-dual-harness.md).
