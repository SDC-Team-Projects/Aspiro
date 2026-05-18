package com.capcom.aspiro.api.dto.request;

import com.capcom.aspiro.domain.model.enums.TaskStatus;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {

    @NotNull
    private TaskStatus status;
}