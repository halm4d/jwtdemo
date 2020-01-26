package com.davidhalma.jwtdemo.onboarding.controller;

import com.davidhalma.jwtdemo.jwtframework.annotation.JwtSecured;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.service.AuthenticationService;
import com.davidhalma.jwtdemo.onboarding.service.JwtAuthenticationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
@Log4j2
public class AuthenticationController {

    @Autowired
    private JwtAuthenticationService jwtAuthenticationService;
    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/login")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) throws Exception {
        return ResponseEntity.ok(jwtAuthenticationService.getJwtToken(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.register(authenticationRequest));
    }

    @JwtSecured
    @PostMapping("/refreshToken")
    public ResponseEntity<?> refreshToken(@RequestHeader("refresh-token") String refreshToken) throws Exception {
        return ResponseEntity.ok(jwtAuthenticationService.refreshToken(refreshToken));
    }

    @JwtSecured
    @GetMapping("/securedpage")
    public ResponseEntity<?> securedpage(){
        log.info("securedpage");
        return ResponseEntity.ok("This page is secret.");
    }

}
