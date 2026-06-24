<!--
REFLECTION_LOG.md — append a short reflection after completing a unit of work.

Entry format:
## YYYY-MM-DD — <short title>
- **Context:** what was being done
- **Surprise:** what was unexpected (especially framework behaviour)
- **Learning:** what future agents/humans should know
- **Improvement:** what to change in HARNESS.md / AGENTS.md / a constraint

Keep entries short. Promote durable facts into AGENTS.md (GOTCHAS) or HARNESS.md (Constraints).
-->

# Reflection Log

## 2026-06-24 — Building ConfPlanner on Embabel 0.5.0
- **Context:** standing up the agent and the full lab branch set.
- **Surprise:** a `post` condition on the goal action does not gate goal achievement at runtime; one
  `@Agent` supports only one goal type; agents plan only with their own actions (not pooled from
  `@EmbabelComponent`).
- **Learning:** enforce invariants by making the goal `pre`-require a `@Condition`; share pipeline
  logic via a `@Service` and give each agent its own thin `@Action` wrappers.
- **Improvement:** captured all of the above as constraints (C5) and GOTCHAS so the next change
  doesn't relearn them.
