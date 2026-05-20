package com.capcom.aspiro.api.service.impl;

import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.request.UpdateTemplateStageRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateStageService;
import com.capcom.aspiro.domain.model.TemplateStage;
import com.capcom.aspiro.domain.repository.TemplateStageRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateStageServiceImpl implements TemplateStageService {

    private final TemplateStageRepository repository;

    @Override
    public void updateStage(Long id, UpdateTemplateStageRequest request) {

        TemplateStage stage = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Stage not found"));

        stage.setTitle(request.getTitle());
        stage.setOrderNumber(request.getOrderNumber());

        repository.save(stage);
    }

    @Override
    public void deleteStage(Long id) {

        repository.deleteById(id);
    }
}