package com.car.appstore.core.updateengine;

import java.util.EnumMap;
import java.util.Map;
import java.util.Set;

public class UpdateStateMachine {

    private static final Set<UpdateState> TERMINAL_STATES = Set.of(
            UpdateState.SUCCEEDED,
            UpdateState.CANCELED
    );

    private static final Map<UpdateState, Map<UpdateEvent, UpdateState>> TRANSITIONS = buildTransitions();

    public UpdateTransitionResult transition(UpdateState current, UpdateEvent event) {
        if (current == null || event == null) {
            return UpdateTransitionResult.noChange(current, event, "state_or_event_is_null");
        }

        if (TERMINAL_STATES.contains(current) && event != UpdateEvent.RETRY) {
            return UpdateTransitionResult.noChange(current, event, "terminal_state_no_transition");
        }

        Map<UpdateEvent, UpdateState> perState = TRANSITIONS.get(current);
        if (perState == null || !perState.containsKey(event)) {
            return UpdateTransitionResult.noChange(current, event, "invalid_transition");
        }

        UpdateState next = perState.get(event);
        return UpdateTransitionResult.changed(current, next, event);
    }

    private static Map<UpdateState, Map<UpdateEvent, UpdateState>> buildTransitions() {
        Map<UpdateState, Map<UpdateEvent, UpdateState>> map = new EnumMap<>(UpdateState.class);

        add(map, UpdateState.IDLE, UpdateEvent.ENQUEUE, UpdateState.QUEUED);
        add(map, UpdateState.QUEUED, UpdateEvent.START, UpdateState.DOWNLOADING);
        add(map, UpdateState.DOWNLOADING, UpdateEvent.DOWNLOAD_COMPLETE, UpdateState.VERIFYING);
        add(map, UpdateState.DOWNLOADING, UpdateEvent.PAUSE, UpdateState.PAUSED);
        add(map, UpdateState.DOWNLOADING, UpdateEvent.CANCEL, UpdateState.CANCELED);

        add(map, UpdateState.PAUSED, UpdateEvent.RESUME, UpdateState.DOWNLOADING);
        add(map, UpdateState.PAUSED, UpdateEvent.CANCEL, UpdateState.CANCELED);

        add(map, UpdateState.VERIFYING, UpdateEvent.VERIFY_PASS, UpdateState.INSTALLING);
        add(map, UpdateState.VERIFYING, UpdateEvent.VERIFY_FAIL, UpdateState.FAILED);

        add(map, UpdateState.INSTALLING, UpdateEvent.INSTALL_SUCCESS, UpdateState.SUCCEEDED);
        add(map, UpdateState.INSTALLING, UpdateEvent.INSTALL_FAIL, UpdateState.FAILED);

        add(map, UpdateState.FAILED, UpdateEvent.RETRY, UpdateState.QUEUED);
        add(map, UpdateState.CANCELED, UpdateEvent.RETRY, UpdateState.QUEUED);
        add(map, UpdateState.SUCCEEDED, UpdateEvent.RETRY, UpdateState.QUEUED);

        return map;
    }

    private static void add(Map<UpdateState, Map<UpdateEvent, UpdateState>> table,
                            UpdateState from,
                            UpdateEvent event,
                            UpdateState to) {
        table.computeIfAbsent(from, ignored -> new EnumMap<>(UpdateEvent.class)).put(event, to);
    }
}
