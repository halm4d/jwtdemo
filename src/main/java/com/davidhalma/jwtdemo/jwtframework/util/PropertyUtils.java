package com.davidhalma.jwtdemo.jwtframework.util;

import com.davidhalma.jwtdemo.jwtframework.property.KeyProperty;
import org.springframework.stereotype.Service;

@Service
public class PropertyUtils {

    private static final String TOKEN_TYPE_NOT_EXIST = "TokenType not exist.";
    private final KeyProperty keyProperty;

    public PropertyUtils(KeyProperty keyProperty) {
        this.keyProperty = keyProperty;
    }

    public String getJksPassword(TokenType tokenType) {
        switch (tokenType){
            case JWT: return getAccess().getJksPassword();
            case REFRESH: return getRefresh().getJksPassword();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public String getKeystoreJks(TokenType tokenType) {
        switch (tokenType){
            case JWT: return getAccess().getKeystoreJks();
            case REFRESH: return getRefresh().getKeystoreJks();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public String getAlias(TokenType tokenType) {
        switch (tokenType){
            case JWT: return getAccess().getAlias();
            case REFRESH: return getRefresh().getAlias();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public String getKeyPassword(TokenType tokenType) {
        switch (tokenType){
            case JWT: return getAccess().getKeyPassword();
            case REFRESH: return getRefresh().getKeyPassword();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public long getExpiration(TokenType tokenType) {
        switch (tokenType) {
            case JWT: return getAccess().getExpiration();
            case REFRESH: return getRefresh().getExpiration();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }
    private KeyProperty.Property getAccess() {
        return keyProperty.getAccess();
    }

    private KeyProperty.Property getRefresh() {
        return keyProperty.getRefresh();
    }
}
