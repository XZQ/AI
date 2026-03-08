package com.car.appstore.core.domain;

public record UpdateInfo(
        AppId appId,
        Version currentVersion,
        Version targetVersion,
        boolean forceUpdate
) {}
