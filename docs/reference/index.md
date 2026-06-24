# Reference

Information-oriented technical description of ConfPlanner. These pages are for looking things up
while you work: exact field names and types, annotation contracts, configuration keys, the catalog
shape, the shell commands, and the branch map. They describe what is there, not how to use it — for
a task, see the [how-to guides](../how-to/index.md); to learn by doing, see the
[tutorials](../tutorials/index.md); for the reasoning behind a design, see the
[explanation](../explanation/index.md).

- **[Domain model](domain-model.md)** — every domain record, its fields and types, and its purpose.
- **[Actions, conditions, and goals](actions-conditions-goals.md)** — the two agents, the shared
  `@Service`, and every `@Action`, `@Condition`, `@AchievesGoal`, and `@SecureAgentTool`.
- **[Configuration](configuration.md)** — `application.yml` keys, Spring and Maven profiles, `.env`
  variables, and the `ProcessOptions` budget.
- **[Catalog schema](catalog-schema.md)** — the JSON shape of the synthetic conference catalog,
  field by field, with counts.
- **[CLI](cli.md)** — the Embabel shell commands and how to run the app.
- **[Branch map](branch-map.md)** — the 13 git refs and the walk order.
