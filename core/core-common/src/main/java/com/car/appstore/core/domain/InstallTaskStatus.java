package com.car.appstore.core.domain;

public enum InstallTaskStatus {
    IDLE,
    DOWNLOADING,
    VERIFYING,
    INSTALLING,
    SUCCEEDED,
    FAILED,
    PAUSED
}
