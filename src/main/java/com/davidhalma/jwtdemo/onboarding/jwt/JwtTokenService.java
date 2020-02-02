package com.davidhalma.jwtdemo.onboarding.jwt;

import com.davidhalma.jwtdemo.jwtframework.exception.JwtTokenException;
import com.davidhalma.jwtdemo.jwtframework.property.AccessTokenProperty;
import com.davidhalma.jwtdemo.jwtframework.property.JwtProperty;
import com.davidhalma.jwtdemo.jwtframework.service.PublicKeyService;
import io.jsonwebtoken.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Date;
import java.util.function.Function;

@Service
@Log4j2
public class JwtTokenService {

    private static final String TOKEN_PREFIX_WITH_WHITESPACE = "Bearer ";

    private final PublicKeyService publicKeyService;
    private final PrivateKeyService privateKeyService;

    @Autowired
    public JwtTokenService(PublicKeyService publicKeyService,
                           PrivateKeyService privateKeyService) {
        this.publicKeyService = publicKeyService;
        this.privateKeyService = privateKeyService;
    }

    public String generateJwtToken(UserDetails userDetails, JwtProperty jwtProperty){
        PrivateKey privateKey = privateKeyService.getKey(jwtProperty);
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setSubject(userDetails.getUsername())
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperty.getExpiration()))
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }

    public String getUsernameFromToken(String token, JwtProperty jwtProperty) {
        return getAllClaimsFromToken(token, jwtProperty).getSubject();
    }

    public Date getExpirationDateFromToken(String token, JwtProperty jwtProperty) {
        return getClaimFromToken(token, jwtProperty, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, JwtProperty jwtProperty, Function<Claims, T> claimsTFunction) {
        return claimsTFunction.apply(getAllClaimsFromToken(token, jwtProperty));
    }

    public Claims getAllClaimsFromToken(String token, JwtProperty jwtProperty) {
        return parseClaimsJws(token, jwtProperty).getBody();
    }

    private Jws<Claims> parseClaimsJws(String token, JwtProperty jwtProperty) {
        try {
            PublicKey publicKey = publicKeyService.getKey(jwtProperty);
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
