package com.davidhalma.jwtdemo.jwtframework.util;

import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Service
@Log4j2
public class JwtUtils {

    private final PropertyUtils propertyUtils;

    public JwtUtils(PropertyUtils propertyUtils) {
        this.propertyUtils = propertyUtils;
    }

    public KeyStore getKeyStore(TokenType tokenType) {
        ClassPathResource resource = new ClassPathResource(propertyUtils.getKeystoreJks(tokenType));
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(resource.getInputStream(), propertyUtils.getJksPassword(tokenType).toCharArray());
            return keystore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
