package com.car.appstore.core.domain;

import java.util.Objects;

public final class DomainError {
    private final String code;
    private final String message;
    private final Throwable cause;

    public DomainError(String code, String message, Throwable cause) {
        this.code = code;
        this.message = message;
        this.cause = cause;
    }

    public String code() {
        return code;
    }

    public String message() {
        return message;
    }

    public Throwable cause() {
        return cause;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DomainError)) return false;
        DomainError that = (DomainError) o;
        return Objects.equals(code, that.code)
                && Objects.equals(message, that.message)
                && Objects.equals(cause, that.cause);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, message, cause);
    }
}
