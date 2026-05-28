package com.capcom.aspiro.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateTemplateStageRequest {

    @NotBlank
    private String title;

    @NotNull
    private Integer orderNumber;
}