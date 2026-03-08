package com.car.appstore.feature.update.domain;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainError;
import com.car.appstore.core.domain.InstallTask;

import java.util.List;
import java.util.Map;

public record BatchUpdateReport(
        List<InstallTask> succeededTasks,
        Map<AppId, DomainError> failedByAppId
) {}
