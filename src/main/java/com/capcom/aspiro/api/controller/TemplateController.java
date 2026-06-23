package com.capcom.aspiro.api.controller;

import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.dto.request.CreateTemplateStageRequest;
import com.capcom.aspiro.api.dto.request.UpdateTemplateRequest;
import com.capcom.aspiro.api.dto.response.DataResponse;
import com.capcom.aspiro.api.dto.response.TemplateResponse;
import com.capcom.aspiro.api.service.interfaces.TemplateService;
import com.capcom.aspiro.api.service.interfaces.TemplateStageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;
    private final TemplateStageService templateStageService;

    @GetMapping
    public ResponseEntity<DataResponse<TemplateResponse>> getTemplates() {
        return ResponseEntity.ok(
                DataResponse.of(templateService.getAllTemplates())
        );
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<DataResponse<TemplateResponse>> getTemplatesForAdmin() {
        return ResponseEntity.ok(
                DataResponse.of(templateService.getAllTemplatesForAdmin())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> getTemplate(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                templateService.getTemplateById(id)
        );
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TemplateResponse> createTemplate(
            @Valid @RequestBody CreateTemplateRequest request
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(templateService.createTemplate(request));
    }

    @PostMapping("/{id}/stages")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> createStage(
            @PathVariable Long id,
            @Valid @RequestBody CreateTemplateStageRequest request
    ) {
        templateStageService.createStage(id, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TemplateResponse> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTemplateRequest request
    ) {
        return ResponseEntity.ok(
                templateService.updateTemplate(id, request)
        );
    }

    @PatchMapping("/{id}/archive")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TemplateResponse> archiveTemplate(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                templateService.archiveTemplate(id)
        );
    }

    @PatchMapping("/{id}/restore")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<TemplateResponse> restoreTemplate(
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(
                templateService.restoreTemplate(id)
        );
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable Long id
    ) {
        templateService.deleteTemplate(id);

        return ResponseEntity.noContent().build();
    }
}