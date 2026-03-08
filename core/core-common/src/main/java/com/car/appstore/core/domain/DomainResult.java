package com.car.appstore.core.domain;

public sealed interface DomainResult<T> permits DomainResult.Success, DomainResult.Failure {
    record Success<T>(T value) implements DomainResult<T> {}
    record Failure<T>(DomainError error) implements DomainResult<T> {}
}
