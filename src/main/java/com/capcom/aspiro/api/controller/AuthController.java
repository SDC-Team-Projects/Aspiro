package com.capcom.aspiro.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.request.LoginRequest;
import com.capcom.aspiro.api.dto.request.RegisterRequest;
import com.capcom.aspiro.api.dto.response.AuthResponse;
import com.capcom.aspiro.api.dto.response.UserProfileResponse;
import com.capcom.aspiro.api.service.interfaces.AuthService;
import com.capcom.aspiro.api.dto.response.UserProfileResponse;
import org.springframework.security.core.Authentication;
import com.capcom.aspiro.api.dto.request.RefreshTokenRequest;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Validated
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Register new user")
    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterRequest request
    ) {

        authService.register(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Login user and receive JWT token")
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(
            @Valid @RequestBody LoginRequest request
    ) {

        return ResponseEntity.ok(
                authService.login(request)
        );
    }

    @Operation(
        summary = "Get current authenticated user",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
        public ResponseEntity<UserProfileResponse> me(Authentication authentication) {
            return ResponseEntity.ok(
                    authService.getCurrentUser(authentication.getName())
            );
        }

        @PostMapping("/refresh")
            public ResponseEntity<AuthResponse> refresh(
                    @Valid @RequestBody RefreshTokenRequest request
            ) {
                return ResponseEntity.ok(authService.refreshToken(request));
        }
}