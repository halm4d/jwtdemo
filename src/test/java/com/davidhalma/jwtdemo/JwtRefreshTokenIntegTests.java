package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.jwtframework.exception.JwtTokenException;
import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

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

    private String USERNAME = "testUser";
    private String PASSWORD = "testPass123";
    @Autowired
    private JwtTestUtils jwtTestUtils;

    @Before
    public void setUp() throws Exception {
        Optional<MDBUser> optionalMDBUser = jwtTestUtils.getOptionalMDBUser(USERNAME, PASSWORD);
        Mockito.when(userRepository.findByUsername(any())).thenReturn(optionalMDBUser);
    }

    @Test
    public void test_refreshToken_ok() {
        JwtToken jwtTokenFromLogin = jwtTestUtils.getJwtTokenFromLogin(USERNAME, PASSWORD);
         jwtTestUtils.sleep(500);     // Without this sleep the login and refreshToken will generate the same token.
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

    @Test
    public void test_refreshToken_tryingWithAccessToken_throwsException() throws Exception {
        JwtToken jwtTokenFromLogin = jwtTestUtils.getJwtTokenFromLogin(USERNAME, PASSWORD);

        JwtTokenException exception =
                assertThrows(JwtTokenException.class, () -> authenticationController.refreshToken(jwtTokenFromLogin.getAccessToken()));
        assertEquals("Bad JWT token signature.", exception.getMessage());
    }
}
