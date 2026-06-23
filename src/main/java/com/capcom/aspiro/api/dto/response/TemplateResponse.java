package com.capcom.aspiro.api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TemplateResponse {

    private Long id;

    private String title;

    private String description;
    
    private Boolean archived;

    private String coverImageUrl;

    private List<TemplateStageResponse> stages;

}