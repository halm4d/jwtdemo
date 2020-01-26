package com.davidhalma.jwtdemo.onboarding.service;

import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.business.User;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class AuthenticationService {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;


    public MDBUser register(AuthenticationRequest authenticationRequest) {
        MDBUser mdbUser = createBaseMDBUser();
        mdbUser.setUsername(authenticationRequest.getUsername());
        mdbUser.setPassword(passwordEncoder.encode(authenticationRequest.getPassword()));
        return userService.save(mdbUser);
    }

    private MDBUser createBaseMDBUser(){
        MDBUser mdbUser = new MDBUser();
        mdbUser.setAuthorities(Collections.singletonList(new SimpleGrantedAuthority(User.Role.USER.toString())));
        mdbUser.setIsCredentialsNonExpired(true);
        mdbUser.setIsAccountNonExpired(true);
        mdbUser.setIsAccountNonLocked(true);
        mdbUser.setIsEnabled(true);
        return mdbUser;
    }
}
