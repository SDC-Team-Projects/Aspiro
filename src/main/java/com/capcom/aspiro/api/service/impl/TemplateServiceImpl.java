package com.capcom.aspiro.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.request.UpdateTemplateRequest;
import com.capcom.aspiro.api.dto.response.TemplateResponse;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import com.capcom.aspiro.api.service.interfaces.TemplateService;
import com.capcom.aspiro.domain.model.Template;
import com.capcom.aspiro.domain.repository.TemplateRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;

    @Override
    public List<TemplateResponse> getAllTemplates() {

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
    public TemplateResponse updateTemplate(Long id, UpdateTemplateRequest request) {

        Template template = templateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Template not found"));

        template.setTitle(request.getTitle());
        template.setDescription(request.getDescription());

        templateRepository.save(template);

        return mapToResponse(template);
    }

    @Override
    public void deleteTemplate(Long id) {

        templateRepository.deleteById(id);
    }

    private TemplateResponse mapToResponse(Template template) {

        return TemplateResponse.builder()
                .id(template.getId())
                .title(template.getTitle())
                .description(template.getDescription())
                .build();
    }
}