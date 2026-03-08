package com.car.appstore.feature.update.domain;

import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainResult;

import java.util.List;

@FunctionalInterface
public interface BatchUpdateUseCase {
    DomainResult<BatchUpdateReport> execute(List<AppId> appIds);
}
