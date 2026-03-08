package com.car.appstore.feature.update.domain;

import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.core.domain.UpdateInfo;

import java.util.List;

@FunctionalInterface
public interface CheckUpdatesUseCase {
    DomainResult<List<UpdateInfo>> execute();
}
