package com.davidhalma.jwtdemo.onboarding.model;

import lombok.Data;

@Data
public class AuthenticationRequest {

    private String username;
    private String password;

}
