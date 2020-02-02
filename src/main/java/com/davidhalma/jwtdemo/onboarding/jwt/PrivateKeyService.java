package com.davidhalma.jwtdemo.onboarding.jwt;

import com.davidhalma.jwtdemo.jwtframework.util.JwtUtils;
import com.davidhalma.jwtdemo.jwtframework.util.KeyService;
import com.davidhalma.jwtdemo.jwtframework.util.PropertyUtils;
import com.davidhalma.jwtdemo.jwtframework.util.TokenType;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.security.*;

@Service
@Log4j2
public class PrivateKeyService implements KeyService {

    private final PropertyUtils propertyUtils;
    private final JwtUtils jwtUtils;

    public PrivateKeyService(JwtUtils jwtUtils, PropertyUtils propertyUtils) {
        this.jwtUtils = jwtUtils;
        this.propertyUtils = propertyUtils;
    }

    @Override
    public Key getKey(TokenType tokenType) {
        KeyStore keystore = jwtUtils.getKeyStore(tokenType);
        try {
            String alias = propertyUtils.getAlias(tokenType);
            String keyPassword = propertyUtils.getKeyPassword(tokenType);
            PrivateKey key = (PrivateKey) keystore.getKey(alias, keyPassword.toCharArray());
            return key;
        } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e) {
            log.error(e.getMessage());
        }
        return null;
    }
}
