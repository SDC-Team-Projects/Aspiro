package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.request.UpdateTemplateTaskRequest;

public interface TemplateTaskService {

    void updateTask(Long id, UpdateTemplateTaskRequest request);

    void deleteTask(Long id);
}