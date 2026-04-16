package com.capcom.aspiro.domain.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.capcom.aspiro.domain.model.enums.GoalStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Goal {
    private Long id;
    private String title;
    private LocalDateTime deadline;
    private GoalStatus status;

    private User user;
    private Template template;

    @Builder.Default
    private List<GoalStage> goalStages = new ArrayList<>();
}
