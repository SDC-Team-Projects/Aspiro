package com.capcom.aspiro.service;

import com.capcom.aspiro.api.dto.request.LoginRequest;
import com.capcom.aspiro.api.dto.request.RegisterRequest;
import com.capcom.aspiro.api.service.impl.AuthServiceImpl;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.UserRepository;
import com.capcom.aspiro.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void register_success() {
        RegisterRequest req = new RegisterRequest();
        req.setName("Alice");
        req.setEmail("alice@example.com");
        req.setPassword("password");

        when(userRepository.existsByEmail("alice@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded");

        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId(1L);
            return user;
        });

        var resp = authService.register(req);

        assertNotNull(resp);
        assertEquals(1L, resp.getId());
        assertEquals("Alice", resp.getName());
        assertEquals("alice@example.com", resp.getEmail());
        assertEquals(UserRole.USER, resp.getRole());

        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_duplicateEmail_throws() {
        RegisterRequest req = new RegisterRequest();
        req.setName("Bob");
        req.setEmail("bob@example.com");
        req.setPassword("password");

        when(userRepository.existsByEmail("bob@example.com")).thenReturn(true);

        assertThrows(RuntimeException.class, () -> authService.register(req));

        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void login_success_returnsToken() {
        LoginRequest req = new LoginRequest();
        req.setEmail("joe@example.com");
        req.setPassword("pass123");

        User user = User.builder().id(5L).name("Joe").email("joe@example.com").password("encoded-pass").role(UserRole.USER).build();

        when(userRepository.findByEmail("joe@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("pass123", "encoded-pass")).thenReturn(true);
        when(jwtService.generateToken("joe@example.com")).thenReturn("tok-A");
        when(jwtService.generateRefreshToken("joe@example.com")).thenReturn("ref-A");

        var resp = authService.login(req);

        assertNotNull(resp);
        assertEquals("tok-A", resp.getAccessToken());
        assertEquals("ref-A", resp.getRefreshToken());
        assertEquals(86400000L, resp.getExpiresIn());
    }

    @Test
    void login_invalidCredentials_throws() {
        LoginRequest req = new LoginRequest();
        req.setEmail("noone@example.com");
        req.setPassword("x");

        when(userRepository.findByEmail("noone@example.com")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> authService.login(req));
    }

    @Test
    void login_wrongPassword_throws() {
        LoginRequest req = new LoginRequest();
        req.setEmail("jane@example.com");
        req.setPassword("wrong");

        User user = User.builder().id(10L).name("Jane").email("jane@example.com").password("encoded-pass").role(UserRole.USER).build();

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrong", "encoded-pass")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> authService.login(req));
    }

    @Test
    void refreshToken_valid_returnsNewTokens() {
        var email = "sam@example.com";
        var refresh = "refresh-abc";

        var req = new com.capcom.aspiro.api.dto.request.RefreshTokenRequest();
        req.setRefreshToken(refresh);

        User user = User.builder().id(7L).name("Sam").email(email).password("enc").role(UserRole.USER).build();

        when(jwtService.extractEmail(refresh)).thenReturn(email);
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(jwtService.isTokenValid(refresh, email)).thenReturn(true);
        when(jwtService.generateToken(email)).thenReturn("new-access");
        when(jwtService.generateRefreshToken(email)).thenReturn("new-refresh");

        var resp = authService.refreshToken(req);

        assertNotNull(resp);
        assertEquals("new-access", resp.getAccessToken());
        assertEquals("new-refresh", resp.getRefreshToken());
        assertEquals(86400000L, resp.getExpiresIn());
    }
}
