package com.davidhalma.jwtdemo.onboarding.jwt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String accessTokenSecret = "changeit";
    private long accessTokenExpiration = 300000;
    private String refreshTokenSecret = "changeit123"; // TODO átvinni az onboardingba
    private long refreshTokenExpiration = 3600000; // TODO átvinni az onboardingba

}
