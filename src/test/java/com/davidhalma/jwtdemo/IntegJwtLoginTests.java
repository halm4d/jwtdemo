package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.exception.AuthenticationException;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.business.User;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = JwtDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class IntegJwtLoginTests {

    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void test_login_ok() throws Exception {
        String username = "testUser";
        String password = "testPass123";

        Mockito.when(userRepository.findByUsername(username)).thenReturn(getOptionalMDBUser(username, password));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);

        JwtToken body = authenticationController.createAuthenticationToken(authenticationRequest)
                .getBody();
        Assertions.assertNotNull(body);
        Assertions.assertNotNull(body.getAccessToken());
        Assertions.assertNotNull(body.getRefreshToken());
        Assertions.assertNotNull(body.getExpirationDate());
        Assertions.assertTrue(body.getExpirationDate().after(new Date()));
    }

    @Test
    public void test_login_userNotFoundException() throws Exception {
        String username = "testUser";
        String password = "testPass123";

        Mockito.when(userRepository.findByUsername(any())).thenReturn(Optional.empty());

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);

        AuthenticationException exception = assertThrows(AuthenticationException.class,
                () -> authenticationController.createAuthenticationToken(authenticationRequest));
        assertEquals("Wrong username or password.", exception.getMessage());
    }

    private Optional<MDBUser> getOptionalMDBUser(String username, String password){
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
        return Optional.of(mdbUser);
    }
}
