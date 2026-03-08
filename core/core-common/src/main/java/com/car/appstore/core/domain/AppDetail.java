package com.car.appstore.core.domain;

import java.util.List;

public record AppDetail(
        AppInfo appInfo,
        String description,
        List<String> screenshots,
        List<String> permissions,
        String changelog
) {}
