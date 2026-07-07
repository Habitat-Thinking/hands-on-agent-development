package com.russmiles.confplanner.shell;

import com.embabel.agent.api.invocation.AgentInvocation;
import com.embabel.agent.core.AgentPlatform;
import com.embabel.agent.domain.io.UserInput;
import com.russmiles.confplanner.domain.PersonalSchedule;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

/**
 * A convenience shell command that invokes the planner programmatically.
 *
 * <p>You can always drive the agent with the built-in {@code x "..."} / {@code execute "..."}
 * commands (which let the platform match your request to a goal). This {@code plan} command
 * shows the other half &mdash; how a real application calls a specific goal directly via
 * {@link AgentInvocation}, asking for a {@link PersonalSchedule} result type.
 */
@ShellComponent
public record ConfPlannerShell(AgentPlatform agentPlatform) {

    @ShellMethod(key = "plan", value = "Plan a personal UberConf schedule from a free-text request")
    public String plan(
            @ShellOption(defaultValue = "I'm a senior platform engineer into Kubernetes, "
                    + "resilience and DevEx; build me a schedule") String request) {
        var schedule = AgentInvocation
                .create(agentPlatform, PersonalSchedule.class)
                .invoke(new UserInput(request));
        return schedule.getContent();
    }
}
