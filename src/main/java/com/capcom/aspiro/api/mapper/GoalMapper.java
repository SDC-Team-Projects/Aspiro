package com.capcom.aspiro.api.mapper;

import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.domain.model.Goal;

public class GoalMapper {

    public static GoalResponse toResponse(
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
}