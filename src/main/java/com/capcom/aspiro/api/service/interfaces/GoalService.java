package com.capcom.aspiro.api.service.interfaces;

import java.util.List;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.response.GoalResponse;

public interface GoalService {

    GoalResponse createGoal(CreateGoalRequest request, String userEmail);

    List<GoalResponse> getUserGoals(String userEmail);

    GoalResponse getGoalById(Long id, String userEmail);
}