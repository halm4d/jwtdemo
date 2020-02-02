package com.davidhalma.jwtdemo.jwtframework.util;

import com.davidhalma.jwtdemo.jwtframework.property.AccessKeyProperty;
import com.davidhalma.jwtdemo.jwtframework.property.KeyProperty;
import com.davidhalma.jwtdemo.onboarding.jwt.RefreshKeyProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PropertyUtils {

    private static final String TOKEN_TYPE_NOT_EXIST = "TokenType not exist.";
    @Autowired
    private AccessKeyProperty accessKeyProperty;
    @Autowired
    private RefreshKeyProperty refreshKeyProperty;

    public String getJksPassword(TokenType tokenType) {
        switch (tokenType){
            case ACCESS: return getAccess().getJksPassword();
            case REFRESH: return getRefresh().getJksPassword();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public String getKeystoreJks(TokenType tokenType) {
        switch (tokenType){
            case ACCESS: return getAccess().getKeystoreJks();
            case REFRESH: return getRefresh().getKeystoreJks();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public String getAlias(TokenType tokenType) {
        switch (tokenType){
            case ACCESS: return getAccess().getAlias();
            case REFRESH: return getRefresh().getAlias();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public String getKeyPassword(TokenType tokenType) {
        switch (tokenType){
            case ACCESS: return getAccess().getKeyPassword();
            case REFRESH: return getRefresh().getKeyPassword();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }

    public long getExpiration(TokenType tokenType) {
        switch (tokenType) {
            case ACCESS: return getAccess().getExpiration();
            case REFRESH: return getRefresh().getExpiration();
            default: throw new IllegalArgumentException(TOKEN_TYPE_NOT_EXIST);
        }
    }
    private KeyProperty getAccess() {
        return accessKeyProperty.getKey();
    }

    private KeyProperty getRefresh() {
        return refreshKeyProperty.getKey();
    }
}
