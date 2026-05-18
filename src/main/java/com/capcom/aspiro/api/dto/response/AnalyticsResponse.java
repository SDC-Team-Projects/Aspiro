package com.capcom.aspiro.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AnalyticsResponse {

    private Integer goalProgress;

    private Integer tasksDone;

    private Integer totalTasks;

    private Integer overdueTasks;

    private Integer completedGoals;
}