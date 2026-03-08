package com.car.appstore.core.domain;

public record InstallTask(
        String taskId,
        AppId appId,
        InstallTaskStatus status,
        int progress,
        String errorCode
) {}
