package com.capcom.aspiro.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateResponse {

    private Long id;

    private String title;

    private String description;
}