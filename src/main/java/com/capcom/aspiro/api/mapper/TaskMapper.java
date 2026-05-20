package com.capcom.aspiro.api.mapper;

import com.capcom.aspiro.api.dto.response.TaskResponse;
import com.capcom.aspiro.domain.model.GoalTask;

public class TaskMapper {

    public static TaskResponse toResponse(GoalTask task) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .status(task.getStatus())
                .startDate(task.getStartDate())
                .endDate(task.getEndDate())
                .build();
    }
}