package com.capcom.aspiro.api.dto.response;

import java.time.LocalDate;

import com.capcom.aspiro.domain.model.enums.GoalStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalResponse {

    private Long id;

    private String title;

    private GoalStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer progress;
}