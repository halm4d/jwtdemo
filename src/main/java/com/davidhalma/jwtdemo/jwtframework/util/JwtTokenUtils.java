package com.davidhalma.jwtdemo.jwtframework.util;

import com.davidhalma.jwtdemo.jwtframework.config.JwtProperties;
import com.davidhalma.jwtdemo.jwtframework.exception.BadAuthorizationHeaderException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;

@Component
public class JwtTokenUtils {


    @Autowired
    private JwtProperties jwtProperties;

    public String generateJwtToken(UserDetails userDetails){
        return Jwts.builder()
                .setClaims(new HashMap<>())
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret())
                .compact();
    }

    public void validateToken(String token){
        parseClaimsJws(token);
    }

    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(getAllClaimsFromToken(token));
    }

    public Claims getAllClaimsFromToken(String token) {
        return parseClaimsJws(token).getBody();
    }

    private Jws<Claims> parseClaimsJws(String token) {
        try {
            return Jwts.parser().setSigningKey(jwtProperties.getSecret()).parseClaimsJws(token);
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
}
