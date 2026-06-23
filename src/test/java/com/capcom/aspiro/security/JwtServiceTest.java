package com.capcom.aspiro.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;

    private static final String BASE64_SECRET_256 = "AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA=";

    @BeforeEach
    void setUp() throws Exception {
        jwtService = new JwtService();
        setField(jwtService, "jwtSecret", BASE64_SECRET_256);
        setField(jwtService, "jwtExpirationMs", 3_600_000L); // 1 hour
        setField(jwtService, "refreshExpirationMs", 7_200_000L); // 2 hours
    }

    @Test
    void generate_extract_validate_success() {
        String email = "user@example.com";

        String token = jwtService.generateToken(email);

        assertThat(token).isNotBlank();
        assertThat(jwtService.extractEmail(token)).isEqualTo(email);
        assertThat(jwtService.isTokenValid(token, email)).isTrue();
    }

    @Test
    void generateRefreshToken_hasDifferentValue_andValid() {
        String email = "user@example.com";

        String access = jwtService.generateToken(email);
        String refresh = jwtService.generateRefreshToken(email);

        assertThat(refresh).isNotBlank();
        assertThat(refresh).isNotEqualTo(access);
        assertThat(jwtService.isTokenValid(refresh, email)).isTrue();
    }

    @Test
    void isTokenValid_false_whenExpired() throws Exception {
        // Force negative expiration so token is instantly expired
        setField(jwtService, "jwtExpirationMs", -1_000L);
        String email = "exp@example.com";

        String token = jwtService.generateToken(email);

        assertThat(jwtService.isTokenValid(token, email)).isFalse();
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field f = JwtService.class.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(target, value);
    }
}
