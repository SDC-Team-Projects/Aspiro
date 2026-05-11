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
public class TemplateStage {
    private Long id;
    private String title;
    private Integer orderNumber;
    private Template template;

    @Builder.Default
    private List<TemplateTask> templateTasks = new ArrayList<>();
}
