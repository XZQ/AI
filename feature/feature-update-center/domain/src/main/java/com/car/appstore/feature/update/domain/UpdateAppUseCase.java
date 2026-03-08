package com.car.appstore.feature.update.domain;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.InstallTask;

@FunctionalInterface
public interface UpdateAppUseCase {
    DomainResult<InstallTask> execute(AppId appId);
}
