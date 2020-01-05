package com.davidhalma.jwtdemo.jwtframework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String secret = "changeit";
    private long expiration = 300000;
    private String tokenPrefix = "Bearer";
    private String headerKey = "Authorization";

}
