package com.davidhalma.jwtdemo.jwt;

import com.davidhalma.jwtdemo.jwtframework.util.JwtTokenUtils;
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
import static org.mockito.Mockito.doThrow;

@RunWith(SpringRunner.class)
class JwtTokenUtilsTest {

    @Mock
    JwtTokenUtils jwtTokenUtils;

    @BeforeEach
    void setUp() {
        jwtTokenUtils = new JwtTokenUtils();
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
        return jwtTokenUtils.generateJwtToken(generateTestUserDetails());
    }

    @Test
    void generateJwtToken() {
        String token = generateValidToken();
        assertNotNull(token);
        assertNotEquals("", token);
    }

    @Test
    void isValidToken() {
        jwtTokenUtils.validateToken(generateValidToken());
    }

    @Test
    void getUsernameFromToken() {
        assertEquals("username123", jwtTokenUtils.getUsernameFromToken(generateValidToken()));
    }

    @Test
    void getExpirationDateFromToken() {
        assertNotNull(jwtTokenUtils.getExpirationDateFromToken(generateValidToken()));
        assertTrue(jwtTokenUtils.getExpirationDateFromToken(generateValidToken()).after(new Date()));
    }

    @Test
    void getClaimFromToken() {
        assertEquals("username123", jwtTokenUtils.getClaimFromToken(generateValidToken(), Claims::getSubject));
        assertNotNull(jwtTokenUtils.getClaimFromToken(generateValidToken(), Claims::getExpiration));
        assertNotNull(jwtTokenUtils.getClaimFromToken(generateValidToken(), Claims::getIssuedAt));
    }

    @Test
    void getAllClaimsFromToken() {
        assertNotNull(jwtTokenUtils.getAllClaimsFromToken(generateValidToken()));
        assertFalse(jwtTokenUtils.getAllClaimsFromToken(generateValidToken()).isEmpty());
    }
}