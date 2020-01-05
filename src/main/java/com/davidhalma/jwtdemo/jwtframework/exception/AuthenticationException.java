package com.davidhalma.jwtdemo.jwtframework.exception;

public class AuthenticationException extends RuntimeException {
    public AuthenticationException(String s) {
        super(s);
    }
}
