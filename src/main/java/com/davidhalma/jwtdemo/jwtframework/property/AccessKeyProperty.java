package com.davidhalma.jwtdemo.jwtframework.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "jwt.access")
public class AccessKeyProperty {

    private KeyProperty key;

}
