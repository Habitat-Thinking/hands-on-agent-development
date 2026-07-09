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
 * any model sees it, matching a small enumerated set of injection <em>markers</em> so the laziest
 * override attempts are dropped deterministically, with no LLM spend.
 *
 * <p><strong>This is a cheap pre-filter, not a security boundary.</strong> A deny-list of fixed
 * phrases is trivially bypassed — rephrasing, another language, or obfuscation walks straight
 * through it — so nothing here should be deployed as a defence against a determined attacker. Its
 * honest value is dropping high-volume, low-effort garbage before you pay for a model call. The
 * real guarantee against a <em>successful</em> injection is structural and lives elsewhere:
 * {@code noDoubleBooking} is deterministic code the model cannot author, so even a fully poisoned
 * {@code assembleSchedule} cannot produce a schedule that reaches the goal — and that holds whether
 * or not this filter caught anything.
 *
 * <p>It is attached with {@code withGuardRails(...)} on the prompt runner in
 * {@link ConfPlanningCapabilities#extractProfile}, the one place raw user input first meets a model.
 * Belt-and-braces, one altitude up from Lab 1: the DICE filter protects the schedule from a model
 * that slips; this guard cheaply screens input that lies, and the structural invariant catches what
 * it misses.
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
