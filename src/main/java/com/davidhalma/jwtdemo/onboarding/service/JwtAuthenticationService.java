package com.davidhalma.jwtdemo.onboarding.service;

import com.davidhalma.jwtdemo.jwtframework.property.AccessTokenProperty;
import com.davidhalma.jwtdemo.jwtframework.property.JwtProperty;
import com.davidhalma.jwtdemo.jwtframework.service.JwtTokenValidator;
import com.davidhalma.jwtdemo.onboarding.exception.AuthenticationException;
import com.davidhalma.jwtdemo.onboarding.jwt.JwtTokenService;
import com.davidhalma.jwtdemo.onboarding.jwt.RefreshTokenProperty;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.security.JwtUserDetailsService;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {
    public final AuthenticationManager authenticationManager;
    public final JwtUserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;
    private final RefreshTokenProperty refreshTokenProperty;
    private final AccessTokenProperty accessTokenProperty;

    public JwtAuthenticationService(AuthenticationManager authenticationManager,
                                    JwtUserDetailsService userDetailsService,
                                    JwtTokenService jwtTokenService,
                                    RefreshTokenProperty refreshTokenProperty,
                                    AccessTokenProperty accessTokenProperty) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.jwtTokenService = jwtTokenService;
        this.refreshTokenProperty = refreshTokenProperty;
        this.accessTokenProperty = accessTokenProperty;
    }

    public JwtToken getJwtToken(AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return generateJwtToken(authenticationRequest.getUsername());
    }

    public JwtToken refreshToken(String authorizationHeader){
        String username = jwtTokenService.getUsernameFromToken(authorizationHeader, getRefreshTokenProperty());
        return generateJwtToken(username);
    }

    private JwtToken generateJwtToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenService.generateJwtToken(userDetails, getAccessTokenProperty());
        String refreshToken = jwtTokenService.generateJwtToken(userDetails, getRefreshTokenProperty());
        return convertJwtToken(token, refreshToken);
    }

    private JwtToken convertJwtToken(String token, String refreshToken) {
        return JwtToken.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expirationDate(jwtTokenService.getExpirationDateFromToken(token, getAccessTokenProperty()))
                .build();
    }

    private void authenticate(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new AuthenticationException("Account is disabled.");
        } catch (BadCredentialsException e) {
            throw new AuthenticationException("Wrong password.");
        }catch (LockedException e){
            throw new AuthenticationException("Account is locked.");
        }
    }

    private JwtProperty getAccessTokenProperty() {
        return accessTokenProperty.getJwtProperty();
    }

    private JwtProperty getRefreshTokenProperty() {
        return refreshTokenProperty.getJwtProperty();
    }
}