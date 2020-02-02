package com.davidhalma.jwtdemo.jwtframework.property;

import lombok.Data;

@Data
public class KeyProperty {
    private String keystoreJks;
    private String jksPassword;
    private String alias;
    private String keyPassword;
    private long expiration;
}
