package com.davidhalma.jwtdemo.onboarding.jwt;

import com.davidhalma.jwtdemo.jwtframework.service.PublicKeyService;
import com.davidhalma.jwtdemo.jwtframework.exception.JwtTokenException;
import com.davidhalma.jwtdemo.jwtframework.util.PropertyUtils;
import com.davidhalma.jwtdemo.jwtframework.util.TokenType;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.security.PublicKey;
import java.util.Date;
import java.util.function.Function;

@Service
@Log4j2
public class JwtTokenService {

    private static final String TOKEN_PREFIX_WITH_WHITESPACE = "Bearer ";

    private final PublicKeyService publicKeyService;
    private final PrivateKeyService privateKeyService;
    private final PropertyUtils propertyUtils;


    @Autowired
    public JwtTokenService(PublicKeyService publicKeyService, PrivateKeyService privateKeyService, PropertyUtils propertyUtils) {
        this.publicKeyService = publicKeyService;
        this.privateKeyService = privateKeyService;
        this.propertyUtils = propertyUtils;
    }

    public String generateJwtToken(UserDetails userDetails, TokenType tokenType){
        Key privateKey = privateKeyService.getKey(tokenType);
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + propertyUtils.getExpiration(tokenType)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public String getUsernameFromToken(String token, TokenType tokenType) {
        return getAllClaimsFromToken(token, tokenType).getSubject();
    }

    public Date getExpirationDateFromToken(String token, TokenType tokenType) {
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
            PublicKey publicKey = publicKeyService.getKey(tokenType);
            return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(getJwtToken(token));
        }catch (ExpiredJwtException e){
            throw new JwtTokenException("JWT token expired.");
        }catch (IllegalArgumentException e){
            throw new JwtTokenException("JWT token is missing.");
        }catch (MalformedJwtException e){
            throw new JwtTokenException("JWT token is not valid.");
        }catch (UnsupportedJwtException e){
            throw new JwtTokenException("JWT token has no claims.");
        }catch (SignatureException e){
            throw new JwtTokenException("Bad JWT token signature.");
        }
    }

    private String getJwtToken(String requestTokenHeader) {
        return requestTokenHeader.replace(TOKEN_PREFIX_WITH_WHITESPACE, "");
    }
}
