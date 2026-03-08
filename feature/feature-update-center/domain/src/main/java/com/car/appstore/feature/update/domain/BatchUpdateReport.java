package com.car.appstore.feature.update.domain;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainError;
import com.car.appstore.core.domain.InstallTask;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public final class BatchUpdateReport {
    private final List<InstallTask> succeededTasks;
    private final Map<AppId, DomainError> failedByAppId;

    public BatchUpdateReport(List<InstallTask> succeededTasks, Map<AppId, DomainError> failedByAppId) {
        this.succeededTasks = succeededTasks;
        this.failedByAppId = failedByAppId;
    }

    public List<InstallTask> succeededTasks() {
        return succeededTasks;
    }

    public Map<AppId, DomainError> failedByAppId() {
        return failedByAppId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BatchUpdateReport)) return false;
        BatchUpdateReport that = (BatchUpdateReport) o;
        return Objects.equals(succeededTasks, that.succeededTasks)
                && Objects.equals(failedByAppId, that.failedByAppId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(succeededTasks, failedByAppId);
    }
}
