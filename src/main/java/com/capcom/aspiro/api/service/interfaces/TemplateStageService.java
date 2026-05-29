package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.request.CreateTemplateStageRequest;
import com.capcom.aspiro.api.dto.request.UpdateTemplateStageRequest;

public interface TemplateStageService {

    void createStage(Long templateId, CreateTemplateStageRequest request);

    void updateStage(Long id, UpdateTemplateStageRequest request);

    void deleteStage(Long id);
}