package com.car.appstore.feature.home.domain;

import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;

import java.util.List;

public interface HomeRepository {
    DomainResult<List<AppInfo>> loadHomeFeeds();
}
