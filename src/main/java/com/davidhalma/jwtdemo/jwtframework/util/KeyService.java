package com.davidhalma.jwtdemo.jwtframework.util;

import java.security.Key;

public interface KeyService {
    Key getKey(TokenType tokenType);
}
