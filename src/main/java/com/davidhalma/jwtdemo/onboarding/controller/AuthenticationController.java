package com.davidhalma.jwtdemo.onboarding.controller;

import com.davidhalma.jwtdemo.jwtframework.annotation.JwtSecured;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.service.JwtAuthenticationService;
import com.davidhalma.jwtdemo.onboarding.service.UserService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jwt")
@Log4j2
public class AuthenticationController {

    private final JwtAuthenticationService jwtAuthenticationService;
    private final UserService userService;


    public AuthenticationController(JwtAuthenticationService jwtAuthenticationService, UserService userService) {
        this.jwtAuthenticationService = jwtAuthenticationService;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<JwtToken> createAuthenticationToken(@RequestBody AuthenticationRequest authenticationRequest) {
        return ResponseEntity.ok(jwtAuthenticationService.getJwtToken(authenticationRequest));
    }

    @PostMapping("/register")
    public ResponseEntity<MDBUser> register(@RequestBody AuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(userService.register(authenticationRequest));
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<JwtToken> refreshToken(@RequestHeader("Authorization") String refreshToken) {
        return ResponseEntity.ok(jwtAuthenticationService.refreshToken(refreshToken));
    }

    @JwtSecured
    @GetMapping("/securedpage")
    public ResponseEntity<String> securedpage(){
        log.info("securedpage");
        return ResponseEntity.ok("This page is secret.");
    }

}
