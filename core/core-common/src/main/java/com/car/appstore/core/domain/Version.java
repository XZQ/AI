package com.car.appstore.core.domain;

import java.util.Objects;

public final class Version {
    private final String name;
    private final long code;

    public Version(String name, long code) {
        this.name = name;
        this.code = code;
    }

    public String name() {
        return name;
    }

    public long code() {
        return code;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Version)) return false;
        Version version = (Version) o;
        return code == version.code && Objects.equals(name, version.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, code);
    }
}
