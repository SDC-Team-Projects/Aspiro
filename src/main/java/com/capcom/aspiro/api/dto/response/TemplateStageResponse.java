package com.capcom.aspiro.api.dto.response;

import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class TemplateStageResponse {

    private Long id;

    private String title;

    private Integer orderNumber;

    private List<TemplateTaskResponse> tasks;
}