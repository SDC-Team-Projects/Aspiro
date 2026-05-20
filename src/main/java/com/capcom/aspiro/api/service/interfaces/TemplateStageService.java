package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.request.UpdateTemplateStageRequest;

public interface TemplateStageService {

    void updateStage(Long id, UpdateTemplateStageRequest request);

    void deleteStage(Long id);
}