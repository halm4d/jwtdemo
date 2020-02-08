package com.davidhalma.jwtdemo.jwtframework.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@ConfigurationProperties(prefix = "jwt.access")
public class AccessTokenProperty {

    private JwtProperty jwtProperty;

}
