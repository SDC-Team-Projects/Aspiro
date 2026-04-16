package com.capcom.aspiro.domain.model;

import java.time.LocalDateTime;

import com.capcom.aspiro.domain.model.enums.TaskStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GoalTask {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDateTime deadline;
    private Integer orderNumber;

    private GoalStage goalStage;
    private TemplateTask templateTask;
}
