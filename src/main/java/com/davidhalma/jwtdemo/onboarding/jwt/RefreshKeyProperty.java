package com.davidhalma.jwtdemo.onboarding.jwt;

import com.davidhalma.jwtdemo.jwtframework.property.KeyProperty;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.refresh")
public class RefreshKeyProperty {

    private KeyProperty key;

}
