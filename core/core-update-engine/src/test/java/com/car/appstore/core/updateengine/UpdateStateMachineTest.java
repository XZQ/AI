package com.car.appstore.core.updateengine;

public class UpdateStateMachineTest {

    private final UpdateStateMachine machine = new UpdateStateMachine();

    public static void main(String[] args) {
        UpdateStateMachineTest test = new UpdateStateMachineTest();
        test.shouldFollowHappyPath();
        test.shouldPauseAndResume();
        test.shouldRejectInvalidTransition();
        test.shouldRetryFromFailed();
        System.out.println("UpdateStateMachineTest: OK");
    }

    void shouldFollowHappyPath() {
        UpdateState state = UpdateState.IDLE;
        state = machine.transition(state, UpdateEvent.ENQUEUE).to();
        state = machine.transition(state, UpdateEvent.START).to();
        state = machine.transition(state, UpdateEvent.DOWNLOAD_COMPLETE).to();
        state = machine.transition(state, UpdateEvent.VERIFY_PASS).to();
        state = machine.transition(state, UpdateEvent.INSTALL_SUCCESS).to();

        require(state == UpdateState.SUCCEEDED, "expected SUCCEEDED");
    }

    void shouldPauseAndResume() {
        UpdateState state = UpdateState.DOWNLOADING;
        state = machine.transition(state, UpdateEvent.PAUSE).to();
        require(state == UpdateState.PAUSED, "expected PAUSED");

        state = machine.transition(state, UpdateEvent.RESUME).to();
        require(state == UpdateState.DOWNLOADING, "expected DOWNLOADING");
    }

    void shouldRejectInvalidTransition() {
        UpdateTransitionResult result = machine.transition(UpdateState.IDLE, UpdateEvent.START);

        require(!result.changed(), "expected unchanged");
        require("invalid_transition".equals(result.reason()), "expected invalid_transition");
        require(result.to() == UpdateState.IDLE, "expected IDLE");
    }

    void shouldRetryFromFailed() {
        UpdateTransitionResult result = machine.transition(UpdateState.FAILED, UpdateEvent.RETRY);

        require(result.changed(), "expected changed");
        require(result.to() == UpdateState.QUEUED, "expected QUEUED");
    }

    private static void require(boolean cond, String msg) {
        if (!cond) throw new IllegalStateException(msg);
    }
}
