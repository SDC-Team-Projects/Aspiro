package com.capcom.aspiro.api.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.UserResponse;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import com.capcom.aspiro.api.mapper.GoalMapper;
import com.capcom.aspiro.api.service.interfaces.AdminService;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;
import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.GoalRepository;
import com.capcom.aspiro.domain.repository.GoalStageRepository;
import com.capcom.aspiro.domain.repository.GoalTaskRepository;
import com.capcom.aspiro.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final GoalStageRepository goalStageRepository;
    private final GoalTaskRepository goalTaskRepository;

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(user -> UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build()
                )
                .toList();
    }

    @Override
    public List<GoalResponse> getUserGoals(Long userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<Goal> goals = goalRepository.findByUserId(user.getId());

        return goals.stream()
                .map(goal -> {

                    // calculate progress (replicate logic from GoalServiceImpl)
                    List<GoalStage> stages = goalStageRepository.findByGoalId(goal.getId());

                    List<GoalTask> allTasks = new ArrayList<>();

                    for (GoalStage stage : stages) {

                        allTasks.addAll(goalTaskRepository.findByGoalStageId(stage.getId()));
                    }

                    int progress;

                    if (allTasks.isEmpty()) {
                        progress = 0;
                    } else {
                        long completedTasks = allTasks.stream()
                                .filter(task -> task.getStatus() == TaskStatus.DONE)
                                .count();

                        progress = (int) ((completedTasks * 100) / allTasks.size());
                    }

                    return GoalMapper.toSummaryResponse(goal, progress);
                })
                .toList();
    }
}
