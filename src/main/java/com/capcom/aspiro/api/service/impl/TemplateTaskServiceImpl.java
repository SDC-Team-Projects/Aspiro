package com.capcom.aspiro.api.service.impl;

import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import com.capcom.aspiro.api.dto.request.UpdateTemplateTaskRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateTaskService;
import com.capcom.aspiro.domain.model.TemplateTask;
import com.capcom.aspiro.domain.repository.TemplateTaskRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TemplateTaskServiceImpl implements TemplateTaskService {

    private final TemplateTaskRepository repository;

    @Override
    public void updateTask(Long id, UpdateTemplateTaskRequest request) {

        TemplateTask task = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found"));

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setOrderNumber(request.getOrderNumber());
        task.setDaysOffset(request.getDaysOffset());
        task.setDurationDays(request.getDurationDays());

        repository.save(task);
    }

    @Override
    public void deleteTask(Long id) {

        repository.deleteById(id);
    }
}