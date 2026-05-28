package com.capcom.aspiro.api.service.impl;

import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.response.UserResponse;
import com.capcom.aspiro.api.service.interfaces.UserService;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse getCurrentUser() {

        Long currentUserId = 1L;

        User user = userRepository.findById(currentUserId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}