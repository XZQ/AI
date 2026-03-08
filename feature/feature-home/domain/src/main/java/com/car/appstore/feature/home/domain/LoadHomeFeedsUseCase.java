package com.car.appstore.feature.home.domain;

import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;

import java.util.List;

@FunctionalInterface
public interface LoadHomeFeedsUseCase {
    DomainResult<List<AppInfo>> execute();
}
