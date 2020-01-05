package com.davidhalma.jwtdemo.user.security;

import com.davidhalma.jwtdemo.user.converter.UserConverter;
import com.davidhalma.jwtdemo.user.model.db.MDBUser;
import com.davidhalma.jwtdemo.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UserConverter userConverter;
    @Autowired
    private UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        MDBUser user = userService.findByUsername(username);
        return userConverter.to(user);
    }
}
