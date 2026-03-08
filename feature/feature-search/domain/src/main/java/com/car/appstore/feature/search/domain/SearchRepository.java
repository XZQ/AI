package com.car.appstore.feature.search.domain;

import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;

import java.util.List;

public interface SearchRepository {
    DomainResult<List<AppInfo>> search(String keyword);

    DomainResult<List<String>> hotKeywords();
}
