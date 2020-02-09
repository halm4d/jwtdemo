package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.business.User;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@Service
public class JwtTestUtils {

    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public JwtToken getJwtTokenFromLogin(String username, String password) {
        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);

        JwtToken body = authenticationController.createAuthenticationToken(authenticationRequest)
                .getBody();

        assertNotNull(body);
        return body;
    }

    public Optional<MDBUser> getOptionalMDBUser(String username, String password){
        MDBUser mdbUser = getMDBUser(username, password);
        return Optional.of(mdbUser);
    }

    public MDBUser getMDBUser(String username, String password){
        MDBUser mdbUser = new MDBUser();
        mdbUser.setId("1");
        mdbUser.setUsername(username);
        mdbUser.setPassword(passwordEncoder.encode(password));
        mdbUser.setIsEnabled(true);
        mdbUser.setIsCredentialsNonExpired(true);
        mdbUser.setIsAccountNonLocked(true);
        mdbUser.setIsAccountNonExpired(true);
        mdbUser.setAuthorities(Collections.singleton(new SimpleGrantedAuthority(User.RoleEnum.USER.name())));
        mdbUser.setCreatedAt(new Date());
        return mdbUser;
    }

    public void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
