package com.davidhalma.jwtdemo.jwtframework.util;

import com.davidhalma.jwtdemo.jwtframework.config.JwtProperties;
import com.davidhalma.jwtdemo.jwtframework.config.TokenType;
import com.davidhalma.jwtdemo.jwtframework.exception.BadAuthorizationHeaderException;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Component
@Log4j2
public class JwtTokenUtils {

    private static final String AUTHORIZATION_HEADER_KEY = "Authorization";
    private static final String TOKEN_PREFIX_WITH_WHITESPACE = "Bearer ";

    @Autowired
    private JwtProperties jwtProperties;

    public String generateJwtToken(UserDetails userDetails, TokenType tokenType){
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + getExpiration(tokenType)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, getSecret(tokenType))
                .compact();
    }

    private String getSecret(TokenType tokenType) {
        switch (tokenType){
            case JWT: return jwtProperties.getJwtSecret();
            case REFRESH: return jwtProperties.getRefreshTokenSecret();
            default: throw new IllegalArgumentException("TokenType not exist.");
        }
    }

    public void validateToken(String token, TokenType tokenType){
        validateRequestTokenHeader(token);
        parseClaimsJws(token, tokenType);
    }

    public String getUsernameFromToken(String token, TokenType tokenType) {
        return getAllClaimsFromToken(token, tokenType).getSubject();
    }

    public Date getExpirationDateFromToken(String token,TokenType tokenType) {
        return getClaimFromToken(token, tokenType, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, TokenType tokenType, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(getAllClaimsFromToken(token, tokenType));
    }

    public Claims getAllClaimsFromToken(String token, TokenType tokenType) {
        return parseClaimsJws(token, tokenType).getBody();
    }

    private Jws<Claims> parseClaimsJws(String token, TokenType tokenType) {
        try {
            log.info(token);
            log.info(tokenType);
            String secret = getSecret(tokenType);
            log.info(secret);
            return Jwts.parser().setSigningKey(secret).parseClaimsJws(getJwtToken(token));
        }catch (ExpiredJwtException e){
            throw new BadAuthorizationHeaderException("JWT token expired.");
        }catch (IllegalArgumentException e){
            throw new BadAuthorizationHeaderException("JWT token is missing.");
        }catch (MalformedJwtException e){
            throw new BadAuthorizationHeaderException("JWT token is not valid.");
        }catch (UnsupportedJwtException e){
            throw new BadAuthorizationHeaderException("JWT token has no claims.");
        }catch (SignatureException e){
            throw new BadAuthorizationHeaderException("Bad JWT token signature.");
        }
    }

    private void validateRequestTokenHeader(String requestTokenHeader) {
        if (StringUtils.isEmpty(requestTokenHeader)){
            throw new BadAuthorizationHeaderException(AUTHORIZATION_HEADER_KEY + " header is missing.");
        }
        if (!requestTokenHeader.startsWith(TOKEN_PREFIX_WITH_WHITESPACE)){
            throw new BadAuthorizationHeaderException(AUTHORIZATION_HEADER_KEY + " does not begin " + TOKEN_PREFIX_WITH_WHITESPACE.trim() + " prefix.");
        }
    }

    private String getJwtToken(String requestTokenHeader) {
        return requestTokenHeader.replace(TOKEN_PREFIX_WITH_WHITESPACE, "");
    }

    private long getExpiration(TokenType tokenType) {
        switch (tokenType) {
            case JWT: return jwtProperties.getExpiration();
            case REFRESH: return jwtProperties.getRefreshTokenExpiration();
            default: throw new IllegalArgumentException("TokenType not exist.");
        }
    }
}
