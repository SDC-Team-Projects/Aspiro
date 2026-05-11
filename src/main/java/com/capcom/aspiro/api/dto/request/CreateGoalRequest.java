package com.capcom.aspiro.api.dto.request;

import java.time.LocalDate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateGoalRequest {

    @NotNull
    private Long templateId;

    @NotBlank
    private String title;

    @NotNull
    private LocalDate startDate;
}