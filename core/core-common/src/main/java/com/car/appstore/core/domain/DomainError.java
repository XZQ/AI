package com.car.appstore.core.domain;

public record DomainError(String code, String message, Throwable cause) {}
