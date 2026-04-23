package com.capcom.aspiro.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateTask {
    private Long id;
    private String title;
    private String description;
    private Integer orderNumber;
    private TemplateStage templateStage;
}
