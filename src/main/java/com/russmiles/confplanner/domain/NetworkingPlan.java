package com.russmiles.confplanner.domain;

import com.embabel.agent.domain.library.HasContent;

import java.util.List;

/**
 * Who an attendee should try to meet, and why (Lab 5).
 *
 * <p>The goal output of {@code ConfNetworkingAgent}. It is a sibling of {@link PersonalSchedule},
 * produced by a different agent over the same upstream types &mdash; the schedule flow is not touched.
 */
public record NetworkingPlan(
        List<String> peopleToMeet,
        String rationale
) implements HasContent {

    @Override
    public String getContent() {
        var sb = new StringBuilder("# People worth meeting\n\n");
        peopleToMeet.forEach(p -> sb.append("- ").append(p).append('\n'));
        sb.append("\n## Why\n\n").append(rationale).append('\n');
        return sb.toString();
    }
}
