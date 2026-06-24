package com.russmiles.confplanner.domain;

import com.embabel.agent.domain.library.HasContent;

import java.util.List;

/**
 * The goal of the whole agent: a conflict-free personal schedule plus the reasoning behind it.
 *
 * <p>Implementing {@link HasContent} lets the shell render the result as readable Markdown
 * instead of a {@code toString()} dump. The {@code rationale} is first-class output, not an
 * afterthought &mdash; an agent that cannot explain its plan is one you cannot trust ("read the
 * plan, not the vibes").
 */
public record PersonalSchedule(
        List<ScheduleItem> items,
        String rationale
) implements HasContent {

    @Override
    public String getContent() {
        var sb = new StringBuilder("# Your UberConf schedule\n\n");
        items.stream()
                .sorted((a, b) -> a.slot().compareTo(b.slot()))
                .forEach(item -> {
                    var s = item.session();
                    sb.append("- **").append(item.slot()).append("** — ")
                            .append(s.title()).append("  \n")
                            .append("  _").append(s.track()).append(" · ")
                            .append(s.room()).append(" · ")
                            .append(String.join(", ", s.speakers())).append("_\n");
                });
        sb.append("\n## Why this schedule\n\n").append(rationale).append("\n");
        return sb.toString();
    }
}
