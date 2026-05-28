package com.capcom.aspiro.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTemplateTaskRequest {

    @NotBlank
    private String title;

    private String description;

    @NotNull
    private Integer orderNumber;

    @NotNull
    private Integer daysOffset;

    @NotNull
    private Integer durationDays;
}