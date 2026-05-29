package com.capcom.aspiro.api.controller;

import java.util.List;

import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.dto.request.CreateTemplateStageRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateStageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.request.UpdateTemplateRequest;
import com.capcom.aspiro.api.dto.response.TemplateResponse;
import com.capcom.aspiro.api.dto.response.DataResponse;
import com.capcom.aspiro.api.service.interfaces.TemplateService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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

    @GetMapping("/{id}")
    public ResponseEntity<TemplateResponse> getTemplate(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                templateService.getTemplateById(id)
        );
    }

    @PostMapping
    public ResponseEntity<TemplateResponse> createTemplate(
            @Valid @RequestBody CreateTemplateRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(templateService.createTemplate(request));
    }

    @PostMapping("/{id}/stages")
    public ResponseEntity<Void> createStage(
            @PathVariable Long id,
            @Valid @RequestBody CreateTemplateStageRequest request
    ) {

        templateStageService.createStage(id, request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<TemplateResponse> updateTemplate(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTemplateRequest request
    ) {

        return ResponseEntity.ok(
                templateService.updateTemplate(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTemplate(
            @PathVariable Long id
    ) {

        templateService.deleteTemplate(id);

        return ResponseEntity.noContent().build();
    }
}