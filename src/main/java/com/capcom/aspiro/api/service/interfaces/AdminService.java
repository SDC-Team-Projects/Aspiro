package com.capcom.aspiro.api.service.interfaces;

import java.util.List;

import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.UserResponse;

public interface AdminService {

    List<UserResponse> getAllUsers();

    List<GoalResponse> getUserGoals(Long userId);
}