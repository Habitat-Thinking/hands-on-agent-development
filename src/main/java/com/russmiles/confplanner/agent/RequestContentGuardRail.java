package com.russmiles.confplanner.agent;

import com.embabel.agent.api.validation.guardrails.UserInputGuardRail;
import com.embabel.agent.core.Blackboard;
import com.embabel.common.core.validation.ValidationError;
import com.embabel.common.core.validation.ValidationResult;
import com.embabel.common.core.validation.ValidationSeverity;

import java.util.List;
import java.util.Locale;

/**
 * The fifth guardrail shape (Lab 3, going further): a <em>content</em> guardrail.
 *
 * <p>The four shapes in Lab 3 — invariant, precondition, budget, secured tool — all guard the
 * <em>plan</em>. This one guards the <em>content</em>: it screens the raw attendee request before
 * any model sees it, and rejects prompt-injection attempts deterministically, with no LLM spend.
 * It is attached with {@code withGuardRails(...)} on the prompt runner in
 * {@link ConfPlanningCapabilities#extractProfile}, the one place raw user input first meets a model.
 *
 * <p>Belt-and-braces, one altitude up from Lab 1: the DICE filter protects the schedule from a
 * model that slips; this guard protects the model from input that lies.
 */
public class RequestContentGuardRail implements UserInputGuardRail {

    /** Deterministic screen: phrases that mark an attempt to override the agent's instructions. */
    private static final List<String> INJECTION_MARKERS = List.of(
            "ignore previous instructions",
            "ignore all instructions",
            "disregard your instructions",
            "reveal your system prompt",
            "reveal your instructions",
            "you are now"
    );

    @Override
    public String getName() {
        return "attendeeRequestGuard";
    }

    @Override
    public String getDescription() {
        return "Rejects attendee requests that attempt to override the agent's instructions";
    }

    @Override
    public ValidationResult validate(String content, Blackboard blackboard) {
        var lower = content == null ? "" : content.toLowerCase(Locale.ROOT);
        var violations = INJECTION_MARKERS.stream()
                .filter(lower::contains)
                .map(marker -> new ValidationError(
                        "injection.marker",
                        "Request contains a prompt-injection marker: \"" + marker + "\"",
                        ValidationSeverity.ERROR))
                .toList();
        return new ValidationResult(violations.isEmpty(), violations);
    }
}
