package com.capcom.aspiro.api.service.impl;

import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.request.LoginRequest;
import com.capcom.aspiro.api.dto.request.RegisterRequest;
import com.capcom.aspiro.api.dto.response.AuthResponse;
import com.capcom.aspiro.api.service.interfaces.AuthService;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    @Override
    public void register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already in use");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                // NOTE: password stored as plain text for now; replace with encoder later
                .password(request.getPassword())
                .role(UserRole.USER)
                .build();

        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid credentials"));

        if (!user.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }

        // Minimal token for local testing. Replace with real JWT later.
        String token = "fake-token-" + user.getId();

        return AuthResponse.builder()
                .accessToken(token)
                .expiresIn(3600L)
                .build();
    }
}
