package com.capcom.aspiro.api.mapper;

import com.capcom.aspiro.api.dto.response.*;
import com.capcom.aspiro.domain.model.Template;

public class TemplateMapper {

    public static TemplateResponse toDetailsResponse(
            Template template
    ) {

        return TemplateResponse.builder()
                .id(template.getId())
                .title(template.getTitle())
                .description(template.getDescription())
                .stages(
                        template.getTemplateStages()
                                .stream()
                                .map(stage ->
                                        TemplateStageResponse.builder()
                                                .id(stage.getId())
                                                .title(stage.getTitle())
                                                .orderNumber(stage.getOrderNumber())
                                                .tasks(
                                                        stage.getTemplateTasks()
                                                                .stream()
                                                                .map(task ->
                                                                        TemplateTaskResponse.builder()
                                                                                .id(task.getId())
                                                                                .title(task.getTitle())
                                                                                .description(task.getDescription())
                                                                                .orderNumber(task.getOrderNumber())
                                                                                .daysOffset(task.getDaysOffset())
                                                                                .durationDays(task.getDurationDays())
                                                                                .build()
                                                                )
                                                                .toList()
                                                )
                                                .build()
                                )
                                .toList()
                )
                .build();
    }
}