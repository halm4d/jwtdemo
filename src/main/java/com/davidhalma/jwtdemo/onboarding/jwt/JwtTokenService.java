package com.davidhalma.jwtdemo.onboarding.jwt;

import com.davidhalma.jwtdemo.jwtframework.exception.JwtTokenException;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.function.Function;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtTokenService {

    private static final String TOKEN_PREFIX_WITH_WHITESPACE = "Bearer ";

    private final JwtProperties jwtProperties;

    public String generateJwtToken(UserDetails userDetails, TokenType tokenType){
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + getExpiration(tokenType)))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, getSecret(tokenType))
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
            return Jwts.parser().setSigningKey(getSecret(tokenType)).parseClaimsJws(getJwtToken(token));
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

    private String getSecret(TokenType tokenType) {
        switch (tokenType){
            case JWT: return jwtProperties.getAccessTokenSecret();
            case REFRESH: return jwtProperties.getRefreshTokenSecret();
            default: throw new IllegalArgumentException("TokenType not exist.");
        }
    }

    private long getExpiration(TokenType tokenType) {
        switch (tokenType) {
            case JWT: return jwtProperties.getAccessTokenExpiration();
            case REFRESH: return jwtProperties.getRefreshTokenExpiration();
            default: throw new IllegalArgumentException("TokenType not exist.");
        }
    }
}
