package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.jwtframework.exception.JwtTokenException;
import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.model.AuthenticationRequest;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.business.User;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.junit.Before;
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
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest(classes = JwtDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class JwtRefreshTokenIntegTests {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    private String USERNAME = "testUser";
    private String PASSWORD = "testPass123";

    @Before
    public void setUp() throws Exception {
        Optional<MDBUser> optionalMDBUser = getOptionalMDBUser(USERNAME, PASSWORD);
        Mockito.when(userRepository.findByUsername(any())).thenReturn(optionalMDBUser);
    }

    @Test
    public void test_refreshToken_ok() {
        JwtToken jwtTokenFromLogin = getJwtTokenFromLogin(USERNAME, PASSWORD);
        sleep(500);     // Without this sleep the login and refreshToken will generate the same token.
                              // jwtTokenFromLogin.getRefreshToken() == jwtTokenFromRefreshToken.getRefreshToken()
                              // This could not happen in production with different usernames
        JwtToken jwtTokenFromRefreshToken = authenticationController.refreshToken(jwtTokenFromLogin.getRefreshToken()).getBody();

        assertNotNull(jwtTokenFromRefreshToken);
        assertNotNull(jwtTokenFromRefreshToken.getAccessToken());
        assertNotNull(jwtTokenFromRefreshToken.getRefreshToken());
        assertNotNull(jwtTokenFromRefreshToken.getExpirationDate());
        assertTrue(jwtTokenFromRefreshToken.getExpirationDate().after(new Date()));

        assertNotEquals(jwtTokenFromLogin.getAccessToken(), jwtTokenFromRefreshToken.getAccessToken());
        assertNotEquals(jwtTokenFromLogin.getRefreshToken(), jwtTokenFromRefreshToken.getRefreshToken());
        assertNotEquals(jwtTokenFromLogin.getExpirationDate(), jwtTokenFromRefreshToken.getExpirationDate());
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test_refreshToken_tryingWithAccessToken_throwsException() throws Exception {
        JwtToken jwtTokenFromLogin = getJwtTokenFromLogin(USERNAME, PASSWORD);

        JwtTokenException exception =
                assertThrows(JwtTokenException.class, () -> authenticationController.refreshToken(jwtTokenFromLogin.getAccessToken()));
        assertEquals("Bad JWT token signature.", exception.getMessage());
    }


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
