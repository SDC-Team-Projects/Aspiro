package com.capcom.aspiro.api.service.interfaces;

import java.util.List;

import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.dto.request.UpdateTemplateRequest;
import com.capcom.aspiro.api.dto.response.TemplateResponse;

public interface TemplateService {

    List<TemplateResponse> getAllTemplates();

    List<TemplateResponse> getAllTemplatesForAdmin();

    TemplateResponse archiveTemplate(Long id);

    TemplateResponse restoreTemplate(Long id);

    TemplateResponse getTemplateById(Long id);

    TemplateResponse createTemplate(CreateTemplateRequest request);

    TemplateResponse updateTemplate(Long id, UpdateTemplateRequest request);

    void deleteTemplate(Long id);
}