package com.russmiles.confplanner.observability;

import com.embabel.agent.api.event.ActionExecutionResultEvent;
import com.embabel.agent.api.event.AgentProcessEvent;
import com.embabel.agent.api.event.AgentProcessFinishedEvent;
import com.embabel.agent.api.event.AgentProcessPlanFormulatedEvent;
import com.embabel.agent.api.event.AgentProcessStuckEvent;
import com.embabel.agent.api.event.AgenticEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The Lab 4 going-further exercise: observability you <em>build</em>, not just read.
 *
 * <p>The planning log and the Zipkin trace are Embabel's own observability surfaces. This class
 * is the third kind — yours. Any Spring bean implementing {@link AgenticEventListener} is picked
 * up by the agent platform and receives every process event: plans formulated, actions executed
 * (with durations), goals achieved, processes stuck. From those events this recorder keeps one
 * small {@link Recording} per process — planning cycles, replans, action runs, time in actions —
 * and emits a single {@code [flight-recorder]} summary line when the process finishes or goes
 * {@code STUCK}.
 *
 * <p>Two things to notice while working the lab. First, replans are <em>derived</em>: the planner
 * plans after every action, so cycles beyond the first are re-planning — on {@code lab4-broken}
 * this counter races upward while the goal never arrives, which is the whole diagnosis in one
 * number. Second, the listener is passive: it observes the same events the logging personalities
 * render, so everything here works with no Docker, no keys, and shows up even in the mocked
 * integration tests.
 */
@Component
public class PlanFlightRecorder implements AgenticEventListener {

    private static final Logger log = LoggerFactory.getLogger(PlanFlightRecorder.class);

    /** Per-process counters. All access is synchronised on the instance. */
    public static final class Recording {
        private int planningCycles;
        private int actionRuns;
        private Duration timeInActions = Duration.ZERO;

        synchronized void notePlan() {
            planningCycles++;
        }

        synchronized void noteAction(Duration runningTime) {
            actionRuns++;
            if (runningTime != null) {
                timeInActions = timeInActions.plus(runningTime);
            }
        }

        public synchronized int planningCycles() {
            return planningCycles;
        }

        /** The planner plans after every action; every cycle beyond the first is a re-plan. */
        public synchronized int replans() {
            return Math.max(0, planningCycles - 1);
        }

        public synchronized int actionRuns() {
            return actionRuns;
        }

        public synchronized Duration timeInActions() {
            return timeInActions;
        }
    }

    private final Map<String, Recording> recordings = new ConcurrentHashMap<>();

    @Override
    public void onProcessEvent(AgentProcessEvent event) {
        var recording = recordings.computeIfAbsent(keyOf(event), id -> new Recording());
        if (event instanceof AgentProcessPlanFormulatedEvent) {
            recording.notePlan();
        } else if (event instanceof ActionExecutionResultEvent action) {
            recording.noteAction(action.getRunningTime());
        } else if (event instanceof AgentProcessStuckEvent) {
            log.warn("[flight-recorder] {} STUCK — {}", keyOf(event), summarise(recording));
        } else if (event instanceof AgentProcessFinishedEvent) {
            log.info("[flight-recorder] {} finished — {}", keyOf(event), summarise(recording));
        }
    }

    /** Read a process's counters (e.g. from a test); null if no events were seen for that id. */
    public Recording recordingFor(String processId) {
        return recordings.get(processId == null ? "unknown" : processId);
    }

    private static String summarise(Recording r) {
        return "%d planning cycle(s), %d replan(s), %d action run(s), %s in actions".formatted(
                r.planningCycles(), r.replans(), r.actionRuns(), r.timeInActions());
    }

    private static String keyOf(AgentProcessEvent event) {
        return event.getProcessId() == null ? "unknown" : event.getProcessId();
    }
}
