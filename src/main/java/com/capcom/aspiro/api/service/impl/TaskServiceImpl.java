package com.capcom.aspiro.api.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.request.UpdateTaskStatusRequest;
import com.capcom.aspiro.api.dto.response.GoalTaskResponse;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import com.capcom.aspiro.api.mapper.TaskMapper;
import com.capcom.aspiro.api.service.interfaces.TaskService;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;
import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.GoalRepository;
import com.capcom.aspiro.domain.repository.GoalStageRepository;
import com.capcom.aspiro.domain.repository.GoalTaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final GoalTaskRepository goalTaskRepository;
    private final GoalStageRepository goalStageRepository;
    private final GoalRepository goalRepository;

    @Override
    public GoalTaskResponse updateTaskStatus(Long taskId, UpdateTaskStatusRequest request) {

        GoalTask task = goalTaskRepository.findById(taskId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Task not found"
                        )
                );

        task.setStatus(request.getStatus());

        /*
         AUTO OVERDUE CHECK
        */
        if (task.getEndDate().isBefore(LocalDate.now()) && request.getStatus() != TaskStatus.DONE) {

            task.setStatus(TaskStatus.OVERDUE);
        }

        task = goalTaskRepository.save(task);

        updateGoalStatus(task);

        return TaskMapper.toResponse(task);
    }

    private void updateGoalStatus(GoalTask task) {

        Goal goal = task.getGoalStage().getGoal();

        List<GoalStage> stages = goalStageRepository.findByGoalId(goal.getId());

        boolean allCompleted = true;

        for (GoalStage stage : stages) {

            List<GoalTask> tasks = goalTaskRepository.findByGoalStageId(stage.getId());

            for (GoalTask currentTask : tasks) {

                if (currentTask.getStatus() != TaskStatus.DONE) {
                    allCompleted = false;
                    break;
                }
            }
        }
        /*
         ALL TASKS COMPLETED
        */
        if (allCompleted) {

            goal.setStatus(GoalStatus.COMPLETED);

        } else {
            /*
             GOAL OVERDUE
            */
            if (goal.getEndDate().isBefore(LocalDate.now())) {

                goal.setStatus(GoalStatus.OVERDUE);

            } else {
                goal.setStatus(GoalStatus.ACTIVE);
            }
        }

        goalRepository.save(goal);
    }
}