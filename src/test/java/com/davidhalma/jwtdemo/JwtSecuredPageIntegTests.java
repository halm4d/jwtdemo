package com.davidhalma.jwtdemo;

import com.davidhalma.jwtdemo.onboarding.controller.AuthenticationController;
import com.davidhalma.jwtdemo.onboarding.model.JwtToken;
import com.davidhalma.jwtdemo.onboarding.model.db.MDBUser;
import com.davidhalma.jwtdemo.onboarding.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = JwtDemoApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
@AutoConfigureMockMvc
public class JwtSecuredPageIntegTests {

    @MockBean
    private UserRepository userRepository;
    @Autowired
    private AuthenticationController authenticationController;
    @Autowired
    private JwtTestUtils jwtTestUtils;
    @Autowired
    private MockMvc mockMvc;

    private String USERNAME = "testUser";
    private String PASSWORD = "testPass123";

    @Before
    public void setUp() throws Exception {
        Optional<MDBUser> optionalMDBUser = jwtTestUtils.getOptionalMDBUser(USERNAME, PASSWORD);
        Mockito.when(userRepository.findByUsername(any())).thenReturn(optionalMDBUser);
    }

    @Test
    public void test_securedPage_ok() throws Exception {
        JwtToken jwtTokenFromLogin = jwtTestUtils.getJwtTokenFromLogin(USERNAME, PASSWORD);
        MockHttpServletResponse mvcResult = mockMvc.perform(MockMvcRequestBuilders
                .get("/api/jwt/securedpage")
                .header("Authorization", "Bearer " + jwtTokenFromLogin.getAccessToken()))
                .andReturn().getResponse();
        String body = mvcResult.getContentAsString();
        assertNotNull(body);
        assertEquals("This page is secret.", body);
    }

    @Test
    public void test_securedPage_tryingWithRefreshToken_statusIsInternalServerError() throws Exception {
        JwtToken jwtTokenFromLogin = jwtTestUtils.getJwtTokenFromLogin(USERNAME, PASSWORD);
        mockMvc.perform(MockMvcRequestBuilders
                .get("/api/jwt/securedpage")
                .header("Authorization", "Bearer " + jwtTokenFromLogin.getRefreshToken()))
                .andExpect(status().isInternalServerError())
                .andReturn();
    }
}
