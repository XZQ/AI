package com.car.appstore.core.updateengine;

public record UpdateTransitionResult(
        UpdateState from,
        UpdateState to,
        UpdateEvent event,
        boolean changed,
        String reason
) {
    public static UpdateTransitionResult noChange(UpdateState state, UpdateEvent event, String reason) {
        return new UpdateTransitionResult(state, state, event, false, reason);
    }

    public static UpdateTransitionResult changed(UpdateState from, UpdateState to, UpdateEvent event) {
        return new UpdateTransitionResult(from, to, event, true, null);
    }
}
