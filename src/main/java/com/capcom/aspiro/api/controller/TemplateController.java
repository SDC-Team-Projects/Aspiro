package com.capcom.aspiro.api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.response.TemplateResponse;
import com.capcom.aspiro.api.service.interfaces.TemplateService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/templates")
@RequiredArgsConstructor
public class TemplateController {

    private final TemplateService templateService;

    @GetMapping
    public ResponseEntity<List<TemplateResponse>> getTemplates() {

        return ResponseEntity.ok(
                templateService.getAllTemplates()
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
}