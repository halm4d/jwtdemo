package com.davidhalma.jwtdemo.jwtframework.property;

import lombok.Data;

@Data
public class JwtProperty {
    private JksProperty jks;
    private KeyProperty key;
    private long expiration;
}
