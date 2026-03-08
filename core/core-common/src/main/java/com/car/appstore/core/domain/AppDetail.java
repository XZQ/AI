package com.car.appstore.core.domain;

import java.util.List;
import java.util.Objects;

public final class AppDetail {
    private final AppInfo appInfo;
    private final String description;
    private final List<String> screenshots;
    private final List<String> permissions;
    private final String changelog;

    public AppDetail(AppInfo appInfo, String description, List<String> screenshots, List<String> permissions, String changelog) {
        this.appInfo = appInfo;
        this.description = description;
        this.screenshots = screenshots;
        this.permissions = permissions;
        this.changelog = changelog;
    }

    public AppInfo appInfo() {
        return appInfo;
    }

    public String description() {
        return description;
    }

    public List<String> screenshots() {
        return screenshots;
    }

    public List<String> permissions() {
        return permissions;
    }

    public String changelog() {
        return changelog;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppDetail)) return false;
        AppDetail appDetail = (AppDetail) o;
        return Objects.equals(appInfo, appDetail.appInfo)
                && Objects.equals(description, appDetail.description)
                && Objects.equals(screenshots, appDetail.screenshots)
                && Objects.equals(permissions, appDetail.permissions)
                && Objects.equals(changelog, appDetail.changelog);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appInfo, description, screenshots, permissions, changelog);
    }
}
