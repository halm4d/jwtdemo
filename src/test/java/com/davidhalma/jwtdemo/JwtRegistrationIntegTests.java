package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = JwtDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class JwtRegistrationIntegTests {

    @Autowired
    private AuthenticationController authenticationController;
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private JwtTestUtils jwtTestUtils;

    @Test
    public void test_register_ok() throws Exception {
        String username = "testUser";
        String password = "testPass123";

        Mockito.when(userRepository.save(any(MDBUser.class))).thenReturn(jwtTestUtils.getMDBUser(username, password));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);

        MDBUser body = authenticationController.register(authenticationRequest)
                .getBody();
        assertNotNull(body);
        assertNotNull(body.getId());
        assertNotNull(body.getUsername());
        assertNotNull(body.getPassword());
        assertNotNull(body.getAuthorities());
        assertTrue(body.getIsAccountNonExpired());
        assertTrue(body.getIsAccountNonLocked());
        assertTrue(body.getIsCredentialsNonExpired());
        assertTrue(body.getIsEnabled());
        assertTrue(body.getCreatedAt().before(Date.from(new Date().toInstant().plusSeconds(1))));

        assertEquals(username, body.getUsername());
        assertTrue(passwordEncoder.matches(password, body.getPassword()));
    }

    @Test
    public void test_register_userAlreadyExists() throws Exception {
        String username = "testUser";
        String password = "testPass123";

        Mockito.when(userRepository.save(any(MDBUser.class))).thenReturn(jwtTestUtils.getMDBUser(username, password));

        AuthenticationRequest authenticationRequest = new AuthenticationRequest();
        authenticationRequest.setUsername(username);
        authenticationRequest.setPassword(password);

        MDBUser body = authenticationController.register(authenticationRequest)
                .getBody();
        assertNotNull(body);
        assertNotNull(body.getId());
        assertNotNull(body.getUsername());
        assertNotNull(body.getPassword());
        assertNotNull(body.getAuthorities());
        assertTrue(body.getIsAccountNonExpired());
        assertTrue(body.getIsAccountNonLocked());
        assertTrue(body.getIsCredentialsNonExpired());
        assertTrue(body.getIsEnabled());
        assertTrue(body.getCreatedAt().before(new Date()));

        assertEquals(username, body.getUsername());
        assertTrue(passwordEncoder.matches(password, body.getPassword()));
    }
}
