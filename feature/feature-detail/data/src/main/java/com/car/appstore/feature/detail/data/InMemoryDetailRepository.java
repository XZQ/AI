package com.car.appstore.feature.detail.data;

import com.car.appstore.core.domain.AppDetail;
import com.car.appstore.core.domain.AppId;
import com.car.appstore.core.domain.DomainError;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.feature.detail.domain.DetailRepository;

import java.util.HashMap;
import java.util.Map;

public class InMemoryDetailRepository implements DetailRepository {
    private final Map<AppId, AppDetail> detailMap;

    public InMemoryDetailRepository(Map<AppId, AppDetail> detailMap) {
        this.detailMap = new HashMap<>(detailMap);
    }

    @Override
    public DomainResult<AppDetail> getAppDetail(AppId appId) {
        AppDetail detail = detailMap.get(appId);
        if (detail == null) {
            return new DomainResult.Failure<>(new DomainError("DETAIL_NOT_FOUND", "app detail not found", null));
        }
        return new DomainResult.Success<>(detail);
    }
}
