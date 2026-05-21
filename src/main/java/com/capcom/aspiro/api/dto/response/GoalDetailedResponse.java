package com.capcom.aspiro.api.dto.response;

import java.time.LocalDate;
import java.util.List;

import com.capcom.aspiro.domain.model.enums.GoalStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalDetailedResponse {

    private Long id;

    private String title;

    private GoalStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer progress;

    private List<GoalStageResponse> stages;
}
