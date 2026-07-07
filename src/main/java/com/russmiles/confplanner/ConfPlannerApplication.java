package com.russmiles.confplanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Boots the ConfPlanner agent as an Embabel shell application.
 *
 * <p>The {@code embabel-agent-starter-shell} dependency turns this into an interactive shell on
 * startup. Once running, type {@code x "..."} (or {@code execute "..."}) to hand the agent a
 * free-text request, or {@code plan} to run the worked example. Add {@code -p} / {@code -r} for
 * prompt and result verbosity when you want to read the plan rather than the vibes.
 */
@SpringBootApplication
public class ConfPlannerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ConfPlannerApplication.class, args);
    }
}
