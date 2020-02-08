package com.davidhalma.jwtdemo.onboarding.security;

import com.davidhalma.jwtdemo.onboarding.converter.UserConverter;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    private final UserConverter userConverter;
    private final UserService userService;

    public JwtUserDetailsService(UserConverter userConverter, UserService userService) {
        this.userConverter = userConverter;
        this.userService = userService;
    }

    @Override
    public UserDetails loadUserByUsername(String username) {
        MDBUser user = userService.findByUsername(username);
        return userConverter.to(user);
    }
}
