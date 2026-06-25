package com.capcom.aspiro.api.mapper;

import com.capcom.aspiro.api.dto.response.GoalTaskResponse;
import com.capcom.aspiro.api.util.StatusResolver;
import com.capcom.aspiro.domain.model.GoalTask;

public class TaskMapper {

    public static GoalTaskResponse toResponse(GoalTask task) {

        return GoalTaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .status(StatusResolver.resolveTaskStatus(
                        task.getStatus(),
                        task.getEndDate()
                ))
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .build();
    }
}