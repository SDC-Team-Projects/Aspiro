package com.capcom.aspiro.api.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateTaskResponse {

    private Long id;

    private String title;

    private String description;

    private Integer orderNumber;

    private Integer daysOffset;

    private Integer durationDays;
}