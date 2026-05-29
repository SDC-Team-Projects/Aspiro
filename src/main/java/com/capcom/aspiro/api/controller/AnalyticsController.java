package com.capcom.aspiro.api.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.capcom.aspiro.api.dto.response.AnalyticsResponse;
import com.capcom.aspiro.api.service.interfaces.AnalyticsService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics() {

        return ResponseEntity.ok(
                analyticsService.getUserAnalytics()
        );
    }
}