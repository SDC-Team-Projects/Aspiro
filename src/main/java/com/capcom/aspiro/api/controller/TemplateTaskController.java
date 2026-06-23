package com.capcom.aspiro.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.request.UpdateTemplateTaskRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateTaskService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;

@RestController
@RequestMapping("/template-tasks")
@RequiredArgsConstructor
public class TemplateTaskController {

    private final TemplateTaskService templateTaskService;

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTemplateTaskRequest request
    ) {

        templateTaskService.updateTask(id, request);

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id
    ) {

        templateTaskService.deleteTask(id);

        return ResponseEntity.noContent().build();
    }
}