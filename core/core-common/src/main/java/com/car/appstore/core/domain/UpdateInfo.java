package com.car.appstore.core.domain;

import java.util.Objects;

public final class UpdateInfo {
    private final AppId appId;
    private final Version currentVersion;
    private final Version targetVersion;
    private final boolean forceUpdate;

    public UpdateInfo(AppId appId, Version currentVersion, Version targetVersion, boolean forceUpdate) {
        this.appId = appId;
        this.currentVersion = currentVersion;
        this.targetVersion = targetVersion;
        this.forceUpdate = forceUpdate;
    }

    public AppId appId() {
        return appId;
    }

    public Version currentVersion() {
        return currentVersion;
    }

    public Version targetVersion() {
        return targetVersion;
    }

    public boolean forceUpdate() {
        return forceUpdate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UpdateInfo)) return false;
        UpdateInfo that = (UpdateInfo) o;
        return forceUpdate == that.forceUpdate
                && Objects.equals(appId, that.appId)
                && Objects.equals(currentVersion, that.currentVersion)
                && Objects.equals(targetVersion, that.targetVersion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appId, currentVersion, targetVersion, forceUpdate);
    }
}
