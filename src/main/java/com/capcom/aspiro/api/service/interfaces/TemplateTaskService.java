package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.request.CreateTemplateTaskRequest;
import com.capcom.aspiro.api.dto.request.UpdateTemplateTaskRequest;

public interface TemplateTaskService {

    void createTask(Long stageId, CreateTemplateTaskRequest request);

    void updateTask(Long id, UpdateTemplateTaskRequest request);

    void deleteTask(Long id);
}