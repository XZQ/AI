package com.car.appstore.feature.search.domain;

import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;

import java.util.List;

@FunctionalInterface
public interface SearchAppsUseCase {
    DomainResult<List<AppInfo>> execute(String keyword);
}
