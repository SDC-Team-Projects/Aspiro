package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.request.LoginRequest;
import com.capcom.aspiro.api.dto.request.RefreshTokenRequest;
import com.capcom.aspiro.api.dto.request.RegisterRequest;
import com.capcom.aspiro.api.dto.response.AuthResponse;
import com.capcom.aspiro.api.dto.response.UserProfileResponse;
import com.capcom.aspiro.api.dto.response.UserResponse;

public interface AuthService {

    UserResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    UserProfileResponse getCurrentUser(String email);

    AuthResponse refreshToken(RefreshTokenRequest request);
}