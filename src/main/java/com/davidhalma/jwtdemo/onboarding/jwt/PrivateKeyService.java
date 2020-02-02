package com.davidhalma.jwtdemo.onboarding.jwt;

import com.davidhalma.jwtdemo.jwtframework.service.KeyStoreService;
import com.davidhalma.jwtdemo.jwtframework.property.JwtProperty;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
@Log4j2
public class PrivateKeyService {


    private final KeyStoreService keyStoreService;

    public PrivateKeyService(KeyStoreService keyStoreService) {
        this.keyStoreService = keyStoreService;
    }

    public PrivateKey getKey(JwtProperty jwtProperty) {
        KeyStore keystore = keyStoreService.getKeyStore(jwtProperty.getJks());
        try {
            return (PrivateKey) keystore.getKey(jwtProperty.getKey().getAlias(), jwtProperty.getKey().getPassword().toCharArray());
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
