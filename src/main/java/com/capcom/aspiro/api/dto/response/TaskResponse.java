package com.capcom.aspiro.api.dto.response;

import java.time.LocalDate;

import com.capcom.aspiro.domain.model.enums.TaskStatus;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TaskResponse {

    private Long id;

    private String title;

    private TaskStatus status;

    private LocalDate startDate;

    private LocalDate endDate;
}