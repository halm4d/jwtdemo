package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.business.User;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.junit.Test;
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

    @Test
    public void test_register_ok() throws Exception {
        String username = "testUser";
        String password = "testPass123";

        Mockito.when(userRepository.save(any(MDBUser.class))).thenReturn(getMDBUser(username, password));

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

        Mockito.when(userRepository.save(any(MDBUser.class))).thenReturn(getMDBUser(username, password));

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

    private MDBUser getMDBUser(String username, String password){
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
}
