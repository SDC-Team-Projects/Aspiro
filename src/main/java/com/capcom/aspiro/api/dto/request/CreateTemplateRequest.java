package com.capcom.aspiro.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateTemplateRequest {

    @NotBlank
    private String title;

    private String description;
}