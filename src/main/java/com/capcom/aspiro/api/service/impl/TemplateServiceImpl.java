package com.capcom.aspiro.api.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Template not found"
                        )
                );

        return mapToResponse(template);
    }

    private TemplateResponse mapToResponse(
            Template template
    ) {

        return TemplateResponse.builder()
                .id(template.getId())
                .title(template.getTitle())
                .description(template.getDescription())
                .build();
    }
}