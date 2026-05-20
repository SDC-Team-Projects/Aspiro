package com.capcom.aspiro.api.service.interfaces;

import java.util.List;

import com.capcom.aspiro.api.dto.request.UpdateTemplateRequest;
import com.capcom.aspiro.api.dto.response.TemplateResponse;

public interface TemplateService {

    List<TemplateResponse> getAllTemplates();

    TemplateResponse getTemplateById(Long id);

    TemplateResponse updateTemplate(Long id, UpdateTemplateRequest request);

    void deleteTemplate(Long id);
}