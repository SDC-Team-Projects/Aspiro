package com.capcom.aspiro.api.service.interfaces;

import java.util.List;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.GoalDetailedResponse;

public interface GoalService {

    GoalResponse createGoal(CreateGoalRequest request);

    List<GoalResponse> getUserGoals();

    GoalDetailedResponse getGoalById(Long id);
}