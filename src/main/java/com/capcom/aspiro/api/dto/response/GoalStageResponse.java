package com.capcom.aspiro.api.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GoalStageResponse {

    private Long id;

    private String title;

    private Integer orderNumber;

    private List<GoalTaskResponse> tasks;
}