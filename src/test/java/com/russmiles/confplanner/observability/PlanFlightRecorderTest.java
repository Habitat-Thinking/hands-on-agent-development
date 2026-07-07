package com.russmiles.confplanner.observability;

import com.embabel.agent.api.event.ActionExecutionResultEvent;
import com.embabel.agent.api.event.AgentProcessCompletedEvent;
import com.embabel.agent.api.event.AgentProcessPlanFormulatedEvent;
import com.embabel.agent.api.event.AgentProcessStuckEvent;
import com.embabel.agent.core.Action;
import com.embabel.agent.core.ActionStatus;
import com.embabel.agent.core.ActionStatusCode;
import com.embabel.agent.core.AgentProcess;
import com.embabel.plan.Plan;
import com.embabel.plan.WorldState;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * The listener is plain code over events, so it is tested by feeding it real event objects with
 * a mocked {@link AgentProcess} — no platform, no LLM, no keys (habit 7: test the seams). The
 * wiring end of the exercise (the platform delivering events to the bean) is visible in the
 * mocked integration tests' output as {@code [flight-recorder]} lines.
 */
class PlanFlightRecorderTest {

    private final PlanFlightRecorder recorder = new PlanFlightRecorder();

    private AgentProcess processWithId(String id) {
        var process = mock(AgentProcess.class);
        when(process.getId()).thenReturn(id);
        return process;
    }

    private AgentProcessPlanFormulatedEvent planFormulated(AgentProcess process) {
        return new AgentProcessPlanFormulatedEvent(process, mock(WorldState.class), mock(Plan.class));
    }

    @Test
    void countsPlanningCyclesActionsAndDerivesReplans() {
        var process = processWithId("run-1");

        // Three planning cycles with two action runs between them — the planner's normal rhythm.
        recorder.onProcessEvent(planFormulated(process));
        recorder.onProcessEvent(new ActionExecutionResultEvent(process, mock(Action.class),
                new ActionStatus(Duration.ofMillis(120), ActionStatusCode.SUCCEEDED),
                Duration.ofMillis(120)));
        recorder.onProcessEvent(planFormulated(process));
        recorder.onProcessEvent(new ActionExecutionResultEvent(process, mock(Action.class),
                new ActionStatus(Duration.ofMillis(80), ActionStatusCode.SUCCEEDED),
                Duration.ofMillis(80)));
        recorder.onProcessEvent(planFormulated(process));
        recorder.onProcessEvent(new AgentProcessCompletedEvent(process));

        var recording = recorder.recordingFor("run-1");
        assertEquals(3, recording.planningCycles());
        assertEquals(2, recording.replans(), "every planning cycle beyond the first is a re-plan");
        assertEquals(2, recording.actionRuns());
        assertEquals(Duration.ofMillis(200), recording.timeInActions());
    }

    @Test
    void keepsProcessesSeparateAndSurvivesAStuckEnding() {
        var healthy = processWithId("run-a");
        var stuck = processWithId("run-b");

        recorder.onProcessEvent(planFormulated(healthy));
        recorder.onProcessEvent(planFormulated(stuck));
        recorder.onProcessEvent(planFormulated(stuck));
        recorder.onProcessEvent(new AgentProcessStuckEvent(stuck));

        assertEquals(0, recorder.recordingFor("run-a").replans());
        assertEquals(1, recorder.recordingFor("run-b").replans());
    }
}
