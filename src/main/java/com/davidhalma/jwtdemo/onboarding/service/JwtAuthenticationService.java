package com.davidhalma.jwtdemo.onboarding.service;

import com.davidhalma.jwtdemo.onboarding.exception.AuthenticationException;
import com.davidhalma.jwtdemo.jwtframework.util.TokenType;
import com.davidhalma.jwtdemo.jwtframework.service.JwtTokenValidator;
import com.davidhalma.jwtdemo.onboarding.jwt.JwtTokenService;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class JwtAuthenticationService {
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    public JwtTokenValidator jwtTokenUtil;
    @Autowired
    public JwtUserDetailsService userDetailsService;
    @Autowired
    private JwtTokenService jwtTokenService;

    public JwtToken getJwtToken(AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        return generateJwtToken(authenticationRequest.getUsername());
    }

    public JwtToken refreshToken(String authorizationHeader){
        String username = jwtTokenService.getUsernameFromToken(authorizationHeader, TokenType.REFRESH);
        return generateJwtToken(username);
    }

    private JwtToken generateJwtToken(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String token = jwtTokenService.generateJwtToken(userDetails, TokenType.ACCESS);
        String refreshToken = jwtTokenService.generateJwtToken(userDetails, TokenType.REFRESH);
        return convertJwtToken(token, refreshToken);
    }

    private JwtToken convertJwtToken(String token, String refreshToken) {
        return JwtToken.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expirationDate(jwtTokenService.getExpirationDateFromToken(token, TokenType.ACCESS))
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
}