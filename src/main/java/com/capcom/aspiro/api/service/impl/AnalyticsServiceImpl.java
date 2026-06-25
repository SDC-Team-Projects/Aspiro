package com.capcom.aspiro.api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.response.AnalyticsResponse;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import com.capcom.aspiro.api.service.interfaces.AnalyticsService;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;
import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.GoalRepository;
import com.capcom.aspiro.domain.repository.GoalStageRepository;
import com.capcom.aspiro.domain.repository.GoalTaskRepository;
import com.capcom.aspiro.domain.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {

    private final UserRepository userRepository;
    private final GoalRepository goalRepository;
    private final GoalStageRepository goalStageRepository;
    private final GoalTaskRepository goalTaskRepository;

    @Override
    public AnalyticsResponse getUserAnalytics(String userEmail) {
        
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        List<Goal> goals = goalRepository.findByUserId(user.getId());

        List<GoalTask> allTasks = new ArrayList<>();

        for (Goal goal : goals) {

            List<GoalStage> stages = goalStageRepository.findByGoalId(goal.getId());

            for (GoalStage stage : stages) {

                allTasks.addAll(goalTaskRepository.findByGoalStageId(stage.getId()));
            }
        }

        int totalTasks = allTasks.size();

        int completedTasks = (int) allTasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.DONE)
                .count();

        int overdueTasks = (int) allTasks.stream()
        .filter(task ->
                task.getStatus() == TaskStatus.OVERDUE
                        ||
                        (
                                task.getEndDate() != null
                                        && task.getEndDate().isBefore(LocalDate.now())
                                        && task.getStatus() != TaskStatus.DONE
                        )
        )
        .count();

        int completedGoals = (int) goals.stream()
                .filter(goal -> goal.getStatus() == GoalStatus.COMPLETED)
                .count();

        int progress = 0;

        if (totalTasks > 0) {

            progress = (completedTasks * 100) / totalTasks;
        }

        return AnalyticsResponse.builder()
                .goalProgress(progress)
                .tasksDone(completedTasks)
                .totalTasks(totalTasks)
                .overdueTasks(overdueTasks)
                .completedGoals(completedGoals)
                .build();
    }
}