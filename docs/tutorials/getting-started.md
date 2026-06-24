# Getting started: your first schedule

In this lesson we clone ConfPlanner, build it green without any API keys, add a single provider key, start the agent's shell, and ask it to plan a personal conference schedule. By the end you will have run a real goal-oriented agent and read both the schedule it produced and the plan it followed to get there.

You do not need to know anything about Embabel, agents, or the codebase. Just follow each step in order. Every command here is meant to work the first time.

!!! note "What you need before we start"
    - **Java 21 or newer.** Nothing else — the repo ships its own Maven via `./mvnw`.
    - About fifteen minutes.
    - An OpenAI API key for the very last steps. We get a green build *without* any key first. (Prefer another provider? Follow [run with a real model](../how-to/run-with-a-real-model.md) instead at Step 4.)

## Step 1 — Get the code

First, clone the repository and move into it.

```bash
git clone https://github.com/russmiles/hands-on-agent-development.git
cd hands-on-agent-development
```

## Step 2 — Check your Java

Now confirm you have Java 21 or newer.

```bash
java -version
```

The output should show version `21` or higher, like this:

```
openjdk version "21.0.2" 2024-01-16
```

If you see a number below 21, install a newer JDK before continuing. You do **not** need to install Maven — the next step uses the wrapper that ships with the repo.

## Step 3 — Build it green, with no keys

Now build the project and run its full test suite.

```bash
./mvnw clean verify
```

This compiles the agent and runs every unit and integration test. The first run downloads dependencies, so give it a minute or two.

Notice that this works with **no API keys and no network model calls**. The tests mock the language model, so the whole suite is green out of the box. The output should end with:

```
BUILD SUCCESS
```

You now have a working build, green without any API key. Next we add one key so the agent can talk to a real model.

## Step 4 — Add one provider key

To run the agent for real, create your `.env` file from the example.

```bash
cp .env.example .env
```

Now open `.env` in your editor and set your OpenAI key:

```bash
OPENAI_API_KEY=sk-...
```

Save the file. `.env` is git-ignored, so your key will not be committed. The app picks up the OpenAI provider automatically — there is nothing else to configure.

## Step 5 — Start the shell

Now start the agent's interactive shell.

```bash
./mvnw spring-boot:run
```

Wait for the startup logs to settle. The output should end with an interactive prompt, something like:

```
shell:>
```

You are now inside the Embabel shell, with the ConfPlanner agent loaded and waiting.

## Step 6 — Ask for a schedule

Now type this command at the `shell:>` prompt exactly as written, then press Enter:

```
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule"
```

The `x` command hands your sentence to the agent and lets it match the request to its goal: a conflict-free personal conference schedule. You will see the agent work through a series of steps and then print a schedule.

The output should look something like this (the exact sessions and wording will vary — the model writes the rationale fresh each time):

```
Your personal UberConf schedule
--------------------------------
Day 1  09:00  Taming Kubernetes Operators at Scale       (Platform, Advanced)
Day 1  11:00  Resilience Patterns for Distributed Systems (Platform, Advanced)
Day 2  09:00  Developer Experience as a Product           (DevEx, Intermediate)
...

Rationale: I leaned into your platform-engineering focus, prioritising the
Kubernetes and resilience tracks, and added a developer-experience session to
match your DevEx interest. No two picks share a slot.
```

Look at the result: the sessions match your interests, **no two share a time slot**, and the agent explains its picks in a short rationale. You just ran a goal-oriented AI agent and got a real, usable schedule.

The no-double-booking is guaranteed, not lucky — but you do not need to know how yet. You will build that guarantee yourself in [Lab 3](walk-the-labs.md#lab-3-guardrails-make-the-contract-bite).

## Step 7 — Read the plan, not the vibes

A schedule is the *what*. Now let us see the *how*. At the `shell:>` prompt, run the same request again, this time with two extra flags:

```
x "I'm a senior platform engineer into Kubernetes, resilience and DevEx; build me a schedule" -p -r
```

`-p` prints the **prompts** the agent sent to the model, and `-r` prints the **results** it got back. Between them you will see the agent's planning log: the ordered steps it chose to reach the goal, something like:

```
extractAttendeeProfile → loadCatalog → shortlistSessions → researchSessions
                       → assembleSchedule → confirmSchedule
```

Nobody wrote that sequence by hand — the agent *derived* it from what each step needs and produces. Reading this plan is how you understand, and later debug, an agent. For *why* the order is derived rather than coded, see [goal-oriented planning](../explanation/goap.md).

## Step 8 — Leave the shell

When you are done exploring, type:

```
exit
```

That stops the agent and returns you to your terminal.

## What you just did

You have:

- built ConfPlanner green with no keys,
- added a provider key and started the agent shell,
- produced a real, conflict-free conference schedule from a single sentence,
- and read the plan the agent derived to get there.

That is the whole loop in miniature. Everything else in the workshop deepens one part of it.

## Where to go next

- **[Walk the labs](walk-the-labs.md)** — follow the agent's growth across six branches, from typed domain models to model routing. This is the natural next lesson.
- Want to *do* a specific thing instead — run in mock mode, turn on Zipkin tracing, switch providers? See the [How-to guides](../how-to/index.md).
- Curious *why* the agent plans instead of running a hard-coded script? Read [About goal-oriented planning](../explanation/goap.md).
- Need to look up a command flag or a config key? See the [CLI reference](../reference/cli.md) and the [configuration reference](../reference/configuration.md).
