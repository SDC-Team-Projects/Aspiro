package com.capcom.aspiro.api.mapper;

import java.util.List;

import com.capcom.aspiro.api.dto.response.*;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;

public class GoalMapper {

    public static GoalResponse toSummaryResponse(
            Goal goal,
            Integer progress
    ) {

        return GoalResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .status(goal.getStatus())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .progress(progress)
                .build();
    }

    public static GoalDetailedResponse toDetailedResponse(
            Goal goal,
            Integer progress,
            List<GoalStage> stages
    ) {

        return GoalDetailedResponse.builder()
                .id(goal.getId())
                .title(goal.getTitle())
                .status(goal.getStatus())
                .startDate(goal.getStartDate())
                .endDate(goal.getEndDate())
                .progress(progress)
                .stages(
                        stages.stream()
                                .map(stage ->
                                        GoalStageResponse.builder()
                                                .id(stage.getId())
                                                .title(stage.getTitle())
                                                .orderNumber(stage.getOrderNumber())
                                                .tasks(
                                                        stage.getGoalTasks()
                                                                .stream()
                                                                .map(TaskMapper::toResponse)
                                                                .toList()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }
}