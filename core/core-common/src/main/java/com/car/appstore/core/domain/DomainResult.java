package com.car.appstore.core.domain;

public interface DomainResult<T> {
    final class Success<T> implements DomainResult<T> {
        private final T value;

        public Success(T value) {
            this.value = value;
        }

        public T value() {
            return value;
        }
    }

    final class Failure<T> implements DomainResult<T> {
        private final DomainError error;

        public Failure(DomainError error) {
            this.error = error;
        }

        public DomainError error() {
            return error;
        }
    }
}
