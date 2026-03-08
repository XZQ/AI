package com.car.appstore.core.domain;

import java.util.Objects;

public final class UserSetting {
    private final boolean autoUpdateEnabled;
    private final boolean notificationEnabled;
    private final NetworkPolicy networkPolicy;

    public UserSetting(boolean autoUpdateEnabled, boolean notificationEnabled, NetworkPolicy networkPolicy) {
        this.autoUpdateEnabled = autoUpdateEnabled;
        this.notificationEnabled = notificationEnabled;
        this.networkPolicy = networkPolicy;
    }

    public boolean autoUpdateEnabled() {
        return autoUpdateEnabled;
    }

    public boolean notificationEnabled() {
        return notificationEnabled;
    }

    public NetworkPolicy networkPolicy() {
        return networkPolicy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserSetting)) return false;
        UserSetting that = (UserSetting) o;
        return autoUpdateEnabled == that.autoUpdateEnabled
                && notificationEnabled == that.notificationEnabled
                && networkPolicy == that.networkPolicy;
    }

    @Override
    public int hashCode() {
        return Objects.hash(autoUpdateEnabled, notificationEnabled, networkPolicy);
    }
}
