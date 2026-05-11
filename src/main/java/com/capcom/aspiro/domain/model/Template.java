package com.capcom.aspiro.domain.model;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Template {
    private Long id;
    private String title;
    private String description;

    @Builder.Default
    private List<TemplateStage> templateStages = new ArrayList<>();
}
