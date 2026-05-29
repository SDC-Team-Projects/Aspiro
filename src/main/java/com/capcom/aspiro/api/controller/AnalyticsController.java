package com.capcom.aspiro.api.controller;

import com.capcom.aspiro.api.dto.response.AnalyticsResponse;
import com.capcom.aspiro.api.service.interfaces.AnalyticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    @GetMapping
    public ResponseEntity<AnalyticsResponse> getAnalytics(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                analyticsService.getUserAnalytics(authentication.getName())
        );
    }
}