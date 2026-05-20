package com.capcom.aspiro.api.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import com.capcom.aspiro.api.mapper.GoalMapper;
import com.capcom.aspiro.api.service.interfaces.GoalService;
import com.capcom.aspiro.domain.model.*;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.*;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final GoalStageRepository goalStageRepository;
    private final GoalTaskRepository goalTaskRepository;

    private final TemplateRepository templateRepository;
    private final TemplateStageRepository templateStageRepository;
    private final TemplateTaskRepository templateTaskRepository;

    private final UserRepository userRepository;

    @Override
    public GoalResponse createGoal(CreateGoalRequest request) {
        /*
         TEMPORARY USER
         UNTIL JWT IS IMPLEMENTED
        */
        User user = userRepository.findById(1L)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Template not found"
                        )
                );

        Goal goal = Goal.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .status(GoalStatus.ACTIVE)
                .user(user)
                .template(template)
                .build();

        goal = goalRepository.save(goal);

        copyStagesAndTasks(goal, template);

        return GoalMapper.toResponse(goal, 0);
    }

    @Override
    public List<GoalResponse> getUserGoals() {

        User user = userRepository.findById(1L)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "User not found"
                        )
                );

        return goalRepository.findByUserId(user.getId())
                .stream()
                .map(goal -> GoalMapper.toResponse(goal, calculateGoalProgress(goal)))
                .toList();
    }

    @Override
    public GoalResponse getGoalById(Long id) {

        Goal goal = goalRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Goal not found"
                        )
                );

        return GoalMapper.toResponse(goal, calculateGoalProgress(goal));
    }

    private void copyStagesAndTasks(Goal goal, Template template) {

        List<TemplateStage> templateStages = templateStageRepository.findByTemplateIdOrderByOrderNumber(template.getId());

        LocalDate latestEndDate = goal.getStartDate();

        for (TemplateStage templateStage : templateStages) {

            GoalStage goalStage = GoalStage.builder()
                    .title(templateStage.getTitle())
                    .orderNumber(templateStage.getOrderNumber())
                    .goal(goal)
                    .templateStage(templateStage)
                    .build();

            goalStage = goalStageRepository.save(goalStage);

            List<TemplateTask> templateTasks = templateTaskRepository.findByTemplateStageIdOrderByOrderNumber(templateStage.getId());

            for (TemplateTask templateTask : templateTasks) {

                LocalDate taskStart = goal.getStartDate()
                                        .plusDays(templateTask.getDaysOffset());

                LocalDate taskEnd = taskStart.plusDays(templateTask.getDurationDays());

                GoalTask goalTask = GoalTask.builder()
                        .title(templateTask.getTitle())
                        .description(templateTask.getDescription())
                        .status(TaskStatus.TODO)
                        .startDate(taskStart)
                        .endDate(taskEnd)
                        .orderNumber(templateTask.getOrderNumber())
                        .goalStage(goalStage)
                        .templateTask(templateTask)
                        .build();

                goalTaskRepository.save(goalTask);

                if (taskEnd.isAfter(latestEndDate)) {
                    latestEndDate = taskEnd;
                }
            }
        }

        goal.setEndDate(latestEndDate);

        goalRepository.save(goal);
    }

    private Integer calculateGoalProgress(Goal goal) {

        List<GoalStage> stages = goalStageRepository.findByGoalId(goal.getId());

        List<GoalTask> allTasks = new ArrayList<>();

        for (GoalStage stage : stages) {

            allTasks.addAll(goalTaskRepository.findByGoalStageId(stage.getId()));
        }

        if (allTasks.isEmpty()) {
            return 0;
        }

        long completedTasks = allTasks.stream()
                .filter(task -> task.getStatus() == TaskStatus.DONE)
                .count();

        return (int) ((completedTasks * 100) / allTasks.size());
    }
}