package com.davidhalma.jwtdemo.jwtframework.service;

import com.davidhalma.jwtdemo.jwtframework.property.JksProperty;
import com.davidhalma.jwtdemo.jwtframework.property.JwtProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;

@Service
@Log4j2
public class PublicKeyService {

    private final KeyStoreService keyStoreService;

    public PublicKeyService(KeyStoreService keyStoreService) {
        this.keyStoreService = keyStoreService;
    }

    public PublicKey getKey(JwtProperty jwtProperty) {
        try {
            String alias = jwtProperty.getKey().getAlias();
            JksProperty jksProperty = jwtProperty.getJks();
            Certificate certificate = keyStoreService.getKeyStore(jksProperty).getCertificate(alias);
            PublicKey publicKey = certificate.getPublicKey();
            return publicKey;
        } catch (KeyStoreException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
