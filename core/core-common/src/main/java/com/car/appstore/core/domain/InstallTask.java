package com.car.appstore.core.domain;

import java.util.Objects;

public final class InstallTask {
    private final String taskId;
    private final AppId appId;
    private final InstallTaskStatus status;
    private final int progress;
    private final String errorCode;

    public InstallTask(String taskId, AppId appId, InstallTaskStatus status, int progress, String errorCode) {
        this.taskId = taskId;
        this.appId = appId;
        this.status = status;
        this.progress = progress;
        this.errorCode = errorCode;
    }

    public String taskId() {
        return taskId;
    }

    public AppId appId() {
        return appId;
    }

    public InstallTaskStatus status() {
        return status;
    }

    public int progress() {
        return progress;
    }

    public String errorCode() {
        return errorCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof InstallTask)) return false;
        InstallTask that = (InstallTask) o;
        return progress == that.progress
                && Objects.equals(taskId, that.taskId)
                && Objects.equals(appId, that.appId)
                && status == that.status
                && Objects.equals(errorCode, that.errorCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, appId, status, progress, errorCode);
    }
}
