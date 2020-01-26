package com.davidhalma.jwtdemo.jwtframework.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String jwtSecret = "changeit";
    private String refreshTokenSecret = "changeit123"; // TODO átvinni az onboardingba
    private long expiration = 300000;
    private long refreshTokenExpiration = 3600000; // TODO átvinni az onboardingba

}
