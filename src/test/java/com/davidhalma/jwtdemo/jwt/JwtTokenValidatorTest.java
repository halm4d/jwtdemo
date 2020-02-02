package com.davidhalma.jwtdemo.jwt;

import com.davidhalma.jwtdemo.jwtframework.service.JwtTokenValidator;
import com.davidhalma.jwtdemo.onboarding.jwt.JwtTokenService;
import com.davidhalma.jwtdemo.onboarding.jwt.TokenType;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collection;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
class JwtTokenValidatorTest {

    @Mock
    JwtTokenValidator jwtTokenValidator;

    @Mock
    JwtTokenService jwtTokenService;

    @BeforeEach
    void setUp() {
        jwtTokenValidator = new JwtTokenValidator();
    }

    private UserDetails generateTestUserDetails(){
        return new UserDetails() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public String getPassword() {
                return null;
            }

            @Override
            public String getUsername() {
                return "username123";
            }

            @Override
            public boolean isAccountNonExpired() {
                return true;
            }

            @Override
            public boolean isAccountNonLocked() {
                return true;
            }

            @Override
            public boolean isCredentialsNonExpired() {
                return true;
            }

            @Override
            public boolean isEnabled() {
                return true;
            }
        };
    }

    private String generateValidToken() {
        return jwtTokenService.generateJwtToken(generateTestUserDetails(), TokenType.ACCESS);
    }

    @Test
    void generateJwtToken() {
        String token = generateValidToken();
        assertNotNull(token);
        assertNotEquals("", token);
    }

    @Test
    void isValidToken() {
        jwtTokenValidator.validateToken(generateValidToken());
    }

    @Test
    void getUsernameFromToken() {
        assertEquals("username123", jwtTokenService.getUsernameFromToken(generateValidToken(), TokenType.ACCESS));
    }

    @Test
    void getExpirationDateFromToken() {
        assertNotNull(jwtTokenService.getExpirationDateFromToken(generateValidToken(), TokenType.ACCESS));
        assertTrue(jwtTokenService.getExpirationDateFromToken(generateValidToken(), TokenType.ACCESS).after(new Date()));
    }

    @Test
    void getClaimFromToken() {
        assertEquals("username123", jwtTokenService.getClaimFromToken(generateValidToken(), TokenType.ACCESS, Claims::getSubject));
        assertNotNull(jwtTokenService.getClaimFromToken(generateValidToken(), TokenType.ACCESS, Claims::getExpiration));
        assertNotNull(jwtTokenService.getClaimFromToken(generateValidToken(), TokenType.ACCESS, Claims::getIssuedAt));
    }

    @Test
    void getAllClaimsFromToken() {
        assertNotNull(jwtTokenService.getAllClaimsFromToken(generateValidToken(), TokenType.ACCESS));
        assertFalse(jwtTokenService.getAllClaimsFromToken(generateValidToken(), TokenType.ACCESS).isEmpty());
    }
}