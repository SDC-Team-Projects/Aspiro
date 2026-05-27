package com.capcom.aspiro.api.service.impl;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.service.interfaces.GoalService;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.repository.GoalRepository;
import com.capcom.aspiro.domain.repository.TemplateRepository;
import com.capcom.aspiro.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;
    private final TemplateRepository templateRepository;

    @Override
    public GoalResponse createGoal(
            CreateGoalRequest request,
            String userEmail
    ) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Goal goal = Goal.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .status(GoalStatus.ACTIVE)
                .user(user)
                .template(
                        templateRepository.findById(request.getTemplateId())
                                .orElseThrow(() -> new RuntimeException("Template not found"))
                )
                .build();

        goalRepository.save(goal);

        return mapToResponse(goal);
    }

    @Override
    public List<GoalResponse> getUserGoals(String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return goalRepository.findByUser(user)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public GoalResponse getGoalById(
            Long id,
            String userEmail
    ) {

        Goal goal = goalRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        return mapToResponse(goal);
    }

    private GoalResponse mapToResponse(Goal goal) {
        return GoalResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .status(goal.getStatus())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .progress(0)
                .build();
    }
}