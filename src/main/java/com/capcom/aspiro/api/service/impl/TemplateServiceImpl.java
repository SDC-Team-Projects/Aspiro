package com.capcom.aspiro.api.service.impl;

import java.util.Comparator;
import java.util.List;

import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.dto.request.UpdateTemplateRequest;
import com.capcom.aspiro.api.dto.response.TemplateResponse;
import com.capcom.aspiro.api.dto.response.TemplateStageResponse;
import com.capcom.aspiro.api.dto.response.TemplateTaskResponse;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import com.capcom.aspiro.api.service.interfaces.TemplateService;
import com.capcom.aspiro.domain.model.Template;
import com.capcom.aspiro.domain.model.TemplateStage;
import com.capcom.aspiro.domain.model.TemplateTask;
import com.capcom.aspiro.domain.repository.TemplateRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;

    @Override
    public List<TemplateResponse> getAllTemplates() {
        return templateRepository.findByArchivedFalse()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public List<TemplateResponse> getAllTemplatesForAdmin() {
        return templateRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TemplateResponse getTemplateById(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        return mapToResponse(template);
    }

    @Override
    public TemplateResponse createTemplate(CreateTemplateRequest request) {
        Template template = Template.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .coverImageUrl(request.getCoverImageUrl())
                .archived(false)
                .build();

        template = templateRepository.save(template);

        return mapToResponse(template);
    }

    @Override
    public TemplateResponse updateTemplate(Long id, UpdateTemplateRequest request) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());
        template.setCoverImageUrl(request.getCoverImageUrl());

        template = templateRepository.save(template);

        return mapToResponse(template);
    }

    @Override
    public TemplateResponse archiveTemplate(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        template.setArchived(true);

        template = templateRepository.save(template);

        return mapToResponse(template);
    }

    @Override
    public TemplateResponse restoreTemplate(Long id) {
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Template not found"));

        template.setArchived(false);

        template = templateRepository.save(template);

        return mapToResponse(template);
    }

    @Override
    public void deleteTemplate(Long id) {
        templateRepository.deleteById(id);
    }

    private TemplateResponse mapToResponse(Template template) {
        List<TemplateStageResponse> stages = template.getTemplateStages() == null
                ? List.of()
                : template.getTemplateStages()
                        .stream()
                        .sorted(Comparator.comparing(TemplateStage::getOrderNumber))
                        .map(this::mapStageToResponse)
                        .toList();

        return TemplateResponse.builder()
                .id(template.getId())
                .title(template.getTitle())
                .description(template.getDescription())
                .coverImageUrl(template.getCoverImageUrl())
                .archived(template.getArchived())
                .stages(stages)
                .build();
    }

    private TemplateStageResponse mapStageToResponse(TemplateStage stage) {
        List<TemplateTaskResponse> tasks = stage.getTemplateTasks() == null
                ? List.of()
                : stage.getTemplateTasks()
                        .stream()
                        .sorted(Comparator.comparing(TemplateTask::getOrderNumber))
                        .map(this::mapTaskToResponse)
                        .toList();

        return TemplateStageResponse.builder()
                .id(stage.getId())
                .title(stage.getTitle())
                .orderNumber(stage.getOrderNumber())
                .tasks(tasks)
                .build();
    }

    private TemplateTaskResponse mapTaskToResponse(TemplateTask task) {
        return TemplateTaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .orderNumber(task.getOrderNumber())
                .daysOffset(task.getDaysOffset())
                .durationDays(task.getDurationDays())
                .build();
    }
}