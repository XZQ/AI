package com.car.appstore.core.updateengine;

public enum UpdateState {
    IDLE,
    QUEUED,
    DOWNLOADING,
    VERIFYING,
    INSTALLING,
    SUCCEEDED,
    FAILED,
    PAUSED,
    CANCELED
}
