package com.car.appstore.core.domain;

public record UserSetting(
        boolean autoUpdateEnabled,
        boolean notificationEnabled,
        NetworkPolicy networkPolicy
) {}
