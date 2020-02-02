package com.davidhalma.jwtdemo.onboarding.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String s) {
        super(s);
    }
}
