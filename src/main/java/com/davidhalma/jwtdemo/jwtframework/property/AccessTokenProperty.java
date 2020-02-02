package com.davidhalma.jwtdemo.jwtframework.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt.access")
public class AccessTokenProperty {

    private JwtProperty jwtProperty;

}
