package com.davidhalma.jwtdemo.jwtframework.service;

import com.davidhalma.jwtdemo.jwtframework.util.JwtUtils;
import com.davidhalma.jwtdemo.jwtframework.util.KeyService;
import com.davidhalma.jwtdemo.jwtframework.util.PropertyUtils;
import com.davidhalma.jwtdemo.jwtframework.util.TokenType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;

@Service
@Log4j2
public class PublicKeyService implements KeyService {

    private final PropertyUtils propertyUtils;
    private final JwtUtils jwtUtils;

    public PublicKeyService(PropertyUtils propertyUtils, JwtUtils jwtUtils) {
        this.propertyUtils = propertyUtils;
        this.jwtUtils = jwtUtils;
    }


    @Override
    public PublicKey getKey(TokenType tokenType) {
        KeyStore keystore = jwtUtils.getKeyStore(tokenType);
        try {
            return keystore != null ? keystore.getCertificate(propertyUtils.getAlias(tokenType)).getPublicKey() : null;
        } catch (KeyStoreException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
