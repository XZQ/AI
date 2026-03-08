package com.car.appstore.feature.detail.domain;

import com.car.appstore.core.domain.AppDetail;
import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainResult;

@FunctionalInterface
public interface GetAppDetailUseCase {
    DomainResult<AppDetail> execute(AppId appId);
}
