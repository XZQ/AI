package com.car.appstore.core.domain;

import java.util.Objects;

public final class AppInfo {
    private final AppId appId;
    private final String name;
    private final String iconUrl;
    private final double rating;
    private final Version version;

    public AppInfo(AppId appId, String name, String iconUrl, double rating, Version version) {
        this.appId = appId;
        this.name = name;
        this.iconUrl = iconUrl;
        this.rating = rating;
        this.version = version;
    }

    public AppId appId() {
        return appId;
    }

    public String name() {
        return name;
    }

    public String iconUrl() {
        return iconUrl;
    }

    public double rating() {
        return rating;
    }

    public Version version() {
        return version;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppInfo)) return false;
        AppInfo appInfo = (AppInfo) o;
        return Double.compare(appInfo.rating, rating) == 0
                && Objects.equals(appId, appInfo.appId)
                && Objects.equals(name, appInfo.name)
                && Objects.equals(iconUrl, appInfo.iconUrl)
                && Objects.equals(version, appInfo.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, name, iconUrl, rating, version);
    }
}
