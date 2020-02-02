package com.davidhalma.jwtdemo.jwtframework.service;

import com.davidhalma.jwtdemo.jwtframework.util.TokenType;

import java.security.Key;

public interface KeyService {
    Key getKey(TokenType tokenType);
}
