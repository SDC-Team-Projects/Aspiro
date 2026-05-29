package com.capcom.aspiro.api.service.impl;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.response.GoalDetailedResponse;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.mapper.GoalMapper;
import com.capcom.aspiro.api.service.interfaces.GoalService;
import com.capcom.aspiro.domain.model.*;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
    public GoalResponse createGoal(CreateGoalRequest request, String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Template template = templateRepository.findById(request.getTemplateId())
                .orElseThrow(() -> new RuntimeException("Template not found"));

        Goal goal = Goal.builder()
                .title(request.getTitle())
                .startDate(request.getStartDate())
                .status(GoalStatus.ACTIVE)
                .user(user)
                .template(template)
                .build();

        goal = goalRepository.save(goal);

        copyStagesAndTasks(goal, template);

        return GoalMapper.toSummaryResponse(goal, calculateGoalProgress(goal));
    }

    @Override
    public List<GoalResponse> getUserGoals(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return goalRepository.findByUserId(user.getId())
                .stream()
                .map(goal -> GoalMapper.toSummaryResponse(goal, calculateGoalProgress(goal)))
                .toList();
    }

    @Override
    public GoalDetailedResponse getGoalById(Long id, String userEmail) {
        Goal goal = goalRepository.findByIdAndUserEmail(id, userEmail)
                .orElseThrow(() -> new RuntimeException("Goal not found"));

        List<GoalStage> stages = goalStageRepository.findByGoalId(goal.getId());

        for (GoalStage stage : stages) {
            stage.setGoalTasks(
                    goalTaskRepository.findByGoalStageId(stage.getId())
            );
        }

        return GoalMapper.toDetailedResponse(
                goal,
                calculateGoalProgress(goal),
                stages
        );
    }

    private void copyStagesAndTasks(Goal goal, Template template) {
        List<TemplateStage> templateStages =
                templateStageRepository.findByTemplateIdOrderByOrderNumber(template.getId());

        LocalDate latestEndDate = goal.getStartDate();

        for (TemplateStage templateStage : templateStages) {
            GoalStage goalStage = GoalStage.builder()
                    .title(templateStage.getTitle())
                    .orderNumber(templateStage.getOrderNumber())
                    .goal(goal)
                    .templateStage(templateStage)
                    .build();

            goalStage = goalStageRepository.save(goalStage);

            List<TemplateTask> templateTasks =
                    templateTaskRepository.findByTemplateStageIdOrderByOrderNumber(templateStage.getId());

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