package com.car.appstore.feature.search.data;

import com.car.appstore.core.domain.AppInfo;
import com.car.appstore.core.domain.DomainResult;
import com.car.appstore.feature.search.domain.SearchRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

public class InMemorySearchRepository implements SearchRepository {
    private final List<AppInfo> allApps;
    private final List<String> hotKeywords;

    public InMemorySearchRepository(List<AppInfo> allApps, List<String> hotKeywords) {
        this.allApps = new ArrayList<>(allApps);
        this.hotKeywords = new ArrayList<>(hotKeywords);
    }

    @Override
    public DomainResult<List<AppInfo>> search(String keyword) {
        String normalized = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        List<AppInfo> matched = allApps.stream()
                .filter(app -> app.name().toLowerCase(Locale.ROOT).contains(normalized))
                .collect(Collectors.toList());
        return new DomainResult.Success<>(matched);
    }

    @Override
    public DomainResult<List<String>> hotKeywords() {
        return new DomainResult.Success<>(Collections.unmodifiableList(hotKeywords));
    }
}
