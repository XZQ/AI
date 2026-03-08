package com.car.appstore.feature.home.data;

import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.feature.home.domain.HomeRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InMemoryHomeRepository implements HomeRepository {
    private final List<AppInfo> homeFeeds;

    public InMemoryHomeRepository(List<AppInfo> homeFeeds) {
        this.homeFeeds = new ArrayList<>(homeFeeds);
    }

    @Override
    public DomainResult<List<AppInfo>> loadHomeFeeds() {
        return new DomainResult.Success<>(Collections.unmodifiableList(homeFeeds));
    }
}
