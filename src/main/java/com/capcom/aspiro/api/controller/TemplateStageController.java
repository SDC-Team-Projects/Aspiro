package com.capcom.aspiro.api.controller;

import com.capcom.aspiro.api.dto.request.CreateTemplateTaskRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateTaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.request.UpdateTemplateStageRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateStageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/template-stages")
@RequiredArgsConstructor
public class TemplateStageController {

    private final TemplateStageService templateStageService;
    private final TemplateTaskService templateTaskService;

    @PostMapping("/{id}/tasks")
    public ResponseEntity<Void> createTask(
            @PathVariable Long id,
            @Valid @RequestBody CreateTemplateTaskRequest request
    ) {

        templateTaskService.createTask(id, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateStage(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTemplateStageRequest request
    ) {

        templateStageService.updateStage(id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStage(
            @PathVariable Long id
    ) {

        templateStageService.deleteStage(id);

        return ResponseEntity.noContent().build();
    }
}