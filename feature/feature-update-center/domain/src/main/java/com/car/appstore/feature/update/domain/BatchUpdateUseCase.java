package com.car.appstore.feature.update.domain;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.InstallTask;

import java.util.List;

@FunctionalInterface
public interface BatchUpdateUseCase {
    DomainResult<List<InstallTask>> execute(List<AppId> appIds);
}
