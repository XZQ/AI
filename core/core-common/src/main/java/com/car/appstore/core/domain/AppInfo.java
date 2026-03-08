package com.car.appstore.core.domain;

public record AppInfo(AppId appId, String name, String iconUrl, double rating, Version version) {}
