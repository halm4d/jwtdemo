package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.exception.AuthenticationException;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = JwtDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class JwtLoginIntegTests {

    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private JwtTestUtils jwtTestUtils;

    @MockBean
    private UserRepository userRepository;

    @Test
    public void test_login_ok() throws Exception {
        String username = "testUser";
        String password = "testPass123";

        Optional<MDBUser> optionalMDBUser = jwtTestUtils.getOptionalMDBUser(username, password);
        Mockito.when(userRepository.findByUsername(username)).thenReturn(optionalMDBUser);

        JwtToken jwtTokenFromLogin = jwtTestUtils.getJwtTokenFromLogin(username, password);

        Assertions.assertNotNull(jwtTokenFromLogin.getAccessToken());
        Assertions.assertNotNull(jwtTokenFromLogin.getRefreshToken());
        Assertions.assertNotNull(jwtTokenFromLogin.getExpirationDate());
        Assertions.assertTrue(jwtTokenFromLogin.getExpirationDate().after(new Date()));
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
}
