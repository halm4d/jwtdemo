package com.davidhalma.jwtdemo.jwtframework.exception;

public class BadAuthorizationHeaderException extends RuntimeException {
    public BadAuthorizationHeaderException(String message) {
        super(message);
    }
}
