package com.davidhalma.jwtdemo.jwtframework.service;

import com.davidhalma.jwtdemo.jwtframework.property.JksProperty;
import com.davidhalma.jwtdemo.jwtframework.property.JwtProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Log4j2
@Service
public class KeyStoreService {

    public KeyStore getKeyStore(JksProperty jksProperty) {
        ClassPathResource resource = new ClassPathResource(jksProperty.getPath());
        try {
            KeyStore keystore = KeyStore.getInstance(KeyStore.getDefaultType());
            keystore.load(resource.getInputStream(), jksProperty.getPassword().toCharArray());
            return keystore;
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            log.error(e.getMessage());
        }
        return null;
    }

}
