package com.capcom.aspiro.api.service.impl;

import com.capcom.aspiro.api.dto.request.LoginRequest;
import com.capcom.aspiro.api.dto.request.RefreshTokenRequest;
import com.capcom.aspiro.api.dto.request.RegisterRequest;
import com.capcom.aspiro.api.dto.response.AuthResponse;
import com.capcom.aspiro.api.dto.response.UserProfileResponse;
import com.capcom.aspiro.api.service.interfaces.AuthService;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.UserRepository;
import com.capcom.aspiro.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static final long ACCESS_TOKEN_EXPIRES_IN = 86400000L;

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        boolean matches = passwordEncoder.matches(
                request.getPassword(),
                user.getPassword()
        );

        if (!matches) {
            throw new RuntimeException("Invalid credentials");
        }

        return buildAuthResponse(user);
    }

    @Override
    public AuthResponse refreshToken(RefreshTokenRequest request) {
        String email = jwtService.extractEmail(request.getRefreshToken());

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!jwtService.isTokenValid(request.getRefreshToken(), user.getEmail())) {
            throw new RuntimeException("Invalid refresh token");
        }

        return buildAuthResponse(user);
    }

    @Override
    public UserProfileResponse getCurrentUser(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserProfileResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private AuthResponse buildAuthResponse(User user) {
        String accessToken = jwtService.generateToken(user.getEmail());
        String refreshToken = jwtService.generateRefreshToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiresIn(ACCESS_TOKEN_EXPIRES_IN)
                .build();
    }
}