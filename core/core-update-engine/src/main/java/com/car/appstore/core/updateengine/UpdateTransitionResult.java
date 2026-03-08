package com.car.appstore.core.updateengine;

public final class UpdateTransitionResult {
    private final UpdateState from;
    private final UpdateState to;
    private final UpdateEvent event;
    private final boolean changed;
    private final String reason;

    public UpdateTransitionResult(UpdateState from, UpdateState to, UpdateEvent event, boolean changed, String reason) {
        this.from = from;
        this.to = to;
        this.event = event;
        this.changed = changed;
        this.reason = reason;
    }

    public static UpdateTransitionResult noChange(UpdateState state, UpdateEvent event, String reason) {
        return new UpdateTransitionResult(state, state, event, false, reason);
    }

    public static UpdateTransitionResult changed(UpdateState from, UpdateState to, UpdateEvent event) {
        return new UpdateTransitionResult(from, to, event, true, null);
    }

    public UpdateState from() {
        return from;
    }

    public UpdateState to() {
        return to;
    }

    public UpdateEvent event() {
        return event;
    }

    public boolean changed() {
        return changed;
    }

    public String reason() {
        return reason;
    }
}
