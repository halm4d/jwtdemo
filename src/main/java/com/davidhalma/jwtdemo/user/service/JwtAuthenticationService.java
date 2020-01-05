package com.davidhalma.jwtdemo.user.service;

import com.davidhalma.jwtdemo.jwtframework.exception.AuthenticationException;
import com.davidhalma.jwtdemo.jwtframework.util.JwtTokenUtils;
import com.davidhalma.jwtdemo.user.model.JwtToken;
import com.davidhalma.jwtdemo.user.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.user.security.JwtUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class JwtAuthenticationService {
    @Autowired
    public AuthenticationManager authenticationManager;
    @Autowired
    public JwtTokenUtils jwtTokenUtil;
    @Autowired
    public JwtUserDetailsService userDetailsService;

    public JwtToken createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final UserDetails userDetails = userDetailsService
                .loadUserByUsername(authenticationRequest.getUsername());

        final String token = jwtTokenUtil.generateJwtToken(userDetails);

        JwtToken jwtToken = new JwtToken();
        jwtToken.setToken(token);
        jwtToken.setExpirationDate(jwtTokenUtil.getExpirationDateFromToken(token));
        return jwtToken;
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