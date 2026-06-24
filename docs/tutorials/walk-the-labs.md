# Walk the labs: a journey through the six branches

In this lesson we walk the agent from its simplest form to its finished shape, one lab at a time. Each lab lives on its own git branch, and each adds exactly one habit. By the end you will have *watched* a passive bag of fields turn into a planning, self-guarding, explainable, extensible, cost-aware agent — and you will understand why each step came when it did.

This page is the map of the journey. The turn-by-turn directions for each leg live in that lab's worksheet on GitHub; we link to each one as we reach it. Follow this page top to bottom, open each worksheet when we point you to it, do the lab, then come back here for the next leg.

!!! note "Before you start the walk"
    Make sure you have a green build and a working shell first — that is exactly what [Getting started](getting-started.md) gives you. You want to be able to run `x "..."` before you start changing the agent.

## How the branches work

Each lab is a pair of branches: a `*-before` branch where the change is *not yet made* (with a worksheet and `// TODO` anchors waiting for you), and a `*-after` branch with the finished, reference version. For each lab you check out the `before` branch, make the change, then diff against the `after` branch to check your work:

```bash
git checkout lab1-before                 # the starting point + the worksheet's TODO anchors
# ...do the lab...
./mvnw -q verify                         # confirm your work builds and passes
git diff lab1-before lab1-after -- src   # compare against the reference solution
```

The journey is the same six steps below, in this order:

```
lab1 → lab2 → lab3 → lab4 → lab5 → lab6
```

(Short on time? You can instead just walk the `*-after` branches in order and read each diff, without editing.)

One lab, Lab 4, starts from a branch that *deliberately fails* — that failing run is the whole point of the lesson. We will flag it when we get there.

Let us begin.

---

## Lab 1 — DICE: give the domain a voice

**Branch:** `lab1-before` → `lab1-after` · **Worksheet:** [labs/lab1-dice.md](https://github.com/russmiles/hands-on-agent-development/blob/main/labs/lab1-dice.md)

We start where every well-built agent starts: with the domain. In the baseline, `AttendeeProfile` is a passive bag of fields, and the rule "this attendee hates vendor keynotes" would have to be copy-pasted into every prompt that needs it.

In this lab **you teach the domain object to speak for itself.** You add an `avoidTopics` field, make `AttendeeProfile` a `PromptContributor`, and let it contribute its own avoid-list into every prompt automatically. You then run `x "...but no vendor keynotes" -p` and *see the avoid-list appear in the prompt log* — proof the behaviour now travels with the data.

**What you learn:** model the domain first. When the rule lives on the object, it follows the object everywhere, and you never have to remember to repeat it. This is DICE — Domain In Context Engineering — and it is the foundation everything else is built on.

When you have a green build and can see the avoid-list in the prompt, come back for Lab 2.

→ Want the deeper "why" behind putting behaviour on domain objects? Read [About domain-driven context (DICE)](../explanation/dice.md).

---

## Lab 2 — GOAP: add an action, watch the plan grow

**Branch:** `lab2-before` → `lab2-after` · **Worksheet:** [labs/lab2-goap.md](https://github.com/russmiles/hands-on-agent-development/blob/main/labs/lab2-goap.md)

Now we meet the heart of the agent: it does not run a script you wrote, it *derives a plan*. In Lab 2 you drop a brand-new `researchSessions` action into the middle of the pipeline — and you do **not** reorder anything by hand.

You add two typed records and one action that consumes `CandidateSessions` and produces `ResearchedSessions`, then re-point the goal to consume the researched output. You rebuild, run `x "..." -p -r`, and read the planning log: the new step has appeared *between* shortlist and assemble, all on its own, because the planner saw that `research` now produces what `assemble` consumes.

There is a small thrill built into this lab: temporarily `return null;` from the new action, re-run, and watch the planner notice the broken link and **replan** instead of crashing. Then put it back.

**What you learn:** name the goal, not the steps. You declare what each action needs and produces; the planner (GOAP — Goal-Oriented Action Planning) finds the order. Add capability and the plan re-routes itself.

→ Curious how the planner actually derives that order? Read [About goal-oriented planning (GOAP)](../explanation/goap.md).

---

## Lab 3 — Guardrails: make the contract bite

**Branch:** `lab3-before` → `lab3-after` · **Worksheet:** [labs/lab3-guardrails.md](https://github.com/russmiles/hands-on-agent-development/blob/main/labs/lab3-guardrails.md)

So far the agent is helpful. Now we make it *trustworthy*. In Lab 3 you make it impossible for the agent to hand back a double-booked schedule, an empty one, or to run forever — and you refuse a premium tool to callers who lack authority.

The centrepiece is a subtle, important move. You learn that in Embabel a `post` condition on the goal action is only a *planning promise*, **not** a runtime gate — the goal counts as achieved the moment its output type is produced. To make the no-double-booking invariant actually *bite*, you split assembly in two:

- `assembleSchedule` produces a `DraftSchedule` with `post = {"noDoubleBooking"}` and `canRerun = true`;
- `confirmSchedule` is the goal action with `pre = {"noDoubleBooking"}`.

Now a clashing draft can never satisfy the goal: the planner re-runs assembly, and if no clash-free arrangement exists it stops at the **budget** rather than returning a broken schedule. You also add a precondition (need candidates first), a `Budget` so it cannot run forever, and a `@SecureAgentTool` access check that denies an unauthorised caller *before* any model spend. You run `GuardrailEnforcementTest` and watch a deliberate clash get refused.

**What you learn:** make the contract explicit, and let the framework — not a code review — enforce it. The assemble → confirm split is the pattern that turns a hopeful prompt into a guaranteed invariant.

→ Want the trade-offs behind framework-enforced guardrails versus prompt instructions? Read [About guardrails](../explanation/guardrails-and-invariants.md).

---

## Lab 4 — Explainability: debug by reading state

**Branch:** `lab4-broken` → `lab4-after` · **Worksheet:** [labs/lab4-explainability.md](https://github.com/russmiles/hands-on-agent-development/blob/main/labs/lab4-explainability.md)

This is the one lab that starts from a branch that is *meant to fail*. `lab4-broken` compiles, but the agent goes `STUCK` at runtime and `./mvnw verify` fails on purpose — that failing run is exactly what you are here to diagnose.

!!! warning "Expect a red build here"
    `lab4-broken` is the single branch in the repo where `./mvnw verify` is *supposed* to fail. Do not try to "fix the build" before you have diagnosed it — diagnosing it *is* the lab.

You will not poke the prompt and re-run. Instead you read the agent's **planning log** and find the condition that never flips to `TRUE`. You will discover that `assembleSchedule` is pinning every item to the placeholder slot `"TBD"`, so `noDoubleBooking` is always false, `confirmSchedule` can never run, and the agent re-runs assembly until the budget stops it. You state the root cause in one sentence *before* touching code, then fix it (each item goes in its session's real slot) and watch the goal complete. If you have Docker, you can also open the Zipkin trace and watch `assembleSchedule` execute over and over while the goal span never closes.

**What you learn:** read the plan, not the vibes. A stuck agent is a *legible* failure — the world-state tells you exactly which condition stayed false. An agent you can read is an agent you can trust and fix.

→ Want to understand the planning log and trace as a model of the agent's mind? Read [About explainability](../explanation/explainability.md). To set up the optional Zipkin trace, see [How to turn on tracing](../how-to/enable-zipkin-tracing.md).

---

## Lab 5 — Extend without breaking (the capstone)

**Branch:** `lab5-before` → `lab5-after` · **Worksheet:** [labs/lab5-extend.md](https://github.com/russmiles/hands-on-agent-development/blob/main/labs/lab5-extend.md)

Now we test whether the agent grows gracefully. You add a whole new capability — a networking plan suggesting people to meet — *without touching* `assembleSchedule`, `confirmSchedule`, or any guardrail. If the schedule's regression test stays green, the seams were in the right place.

Two facts about Embabel 0.5.0 shape the design, and you will feel both:

1. An `@Agent` supports only **one** goal type. `PersonalSchedule` is already taken, so the `NetworkingPlan` goal needs its **own** agent.
2. An agent plans **only with its own actions** — it cannot borrow another agent's. So the new agent must declare the upstream steps it needs.

The clean way to avoid copy-pasted logic is the one you build: lift the shared understand → load → shortlist → research pipeline into a plain `@Service` (`ConfPlanningCapabilities`), then have each agent declare thin `@Action` wrappers that delegate to it. Shared logic, per-agent actions. You invoke the new `NetworkingPlan` goal, see its planning log reuse the existing pipeline (`extract → loadCatalog → shortlist → research → planNetworking`), and confirm the schedule flow is provably unchanged.

**What you learn:** extend by adding, not rewiring. New capability arrives as new types, new actions, and — when a second goal is involved — a new agent. The existing flow stays exactly as it was.

→ Want the reasoning behind a second agent over a second goal in one class? Read [About extending the agent](../explanation/extending-the-agent.md).

---

## Lab 6 — Model routing: the cheapest model that passes

**Branch:** `lab6-before` → `lab6-after` · **Worksheet:** [labs/lab6-model-routing.md](https://github.com/russmiles/hands-on-agent-development/blob/main/labs/lab6-model-routing.md)

The final leg makes cost a design parameter. Not every step needs your strongest model: pulling a few fields out of a sentence is cheap work; synthesising a conflict-free schedule with a rationale is not.

You route the cheap steps (`extractProfile`, `shortlist`) to `withLlmByRole("cheapest")` and the reasoning steps (`research`, `assembleSchedule`) to `withLlmByRole("best")`, then confirm `application.yml` maps those roles to real models (`cheapest: gpt-4.1-nano`, `best: gpt-4.1`). The logic does not change — only the role on each call does, because routing is a *configuration* decision, not a code rewrite. You update `MODEL_ROUTING.md` so the table matches, and because the tests mock the model, the build stays green with no keys.

**What you learn:** right-size the model. Justify the model per action by the complexity of what it returns — a flat list of tags goes cheap, a reasoned schedule goes strong. And the same lever lets you route a sensitive step to a *local* model when its data cannot leave the building.

→ Want the rule of thumb and the data-residency trade-offs? Read [About model routing](../explanation/right-sizing-models.md), and consult the [model routing reference](../reference/model-routing.md) while you work.

---

## You made it — the whole agent, end to end

Step back and look at the journey. You started with a passive data record and finished with an agent that:

- carries its domain rules *on* its domain objects (Lab 1),
- derives its own plan instead of running a script (Lab 2),
- guarantees its contract, not just hopes for it (Lab 3),
- fails legibly and is debugged by reading state (Lab 4),
- grows by adding, never by rewiring (Lab 5),
- and spends the cheapest model that does the job (Lab 6).

Those are the first six habits, made concrete (the [eight-habit stack](../explanation/the-eight-habits.md) adds two — *test the seams* and *govern the loop* — that run throughout). Close every change the way the workshop does: **read the planning log, read the trace, confirm the acceptance check.** That ritual is the whole discipline in three lines.

## Where to go next

- Ready to *get specific jobs done* now — switch providers, run in mock mode, add a new action, expose a secured tool? Head to the [How-to guides](../how-to/index.md).
- Want the bigger picture of how the runtime harness (Embabel) and the build-time harness mirror each other? Read [About the dual harness](../explanation/the-dual-harness.md).
- Need to look something up while you build? The [Reference](../reference/index.md) has the CLI flags, configuration keys, and the model routing table.
