package com.davidhalma.jwtdemo.jwtframework.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "jwt.key")
public class KeyProperty {

    private Property access;
    private Property refresh;

    @Data
    public static class Property {
        private String keystoreJks;
        private String jksPassword;
        private String alias;
        private String keyPassword;
        private long expiration;
    }

}
