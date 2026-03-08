package com.car.appstore.core.domain;

import java.util.Objects;

public final class AppId {
    private final String value;

    public AppId(String value) {
        this.value = value;
    }

    public String value() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AppId)) return false;
        AppId appId = (AppId) o;
        return Objects.equals(value, appId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "AppId{value='" + value + "'}";
    }
}
