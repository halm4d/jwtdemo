package com.davidhalma.jwtdemo.onboarding.jwt;

import com.davidhalma.jwtdemo.jwtframework.property.JwtProperty;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "jwt.refresh")
public class RefreshTokenProperty {

    private JwtProperty jwtProperty;

}
