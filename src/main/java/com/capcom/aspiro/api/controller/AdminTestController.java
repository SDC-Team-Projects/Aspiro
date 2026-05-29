package com.capcom.aspiro.api.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
public class AdminTestController {

    @Operation(
        summary = "Admin-only test endpoint",
        security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/admin/test")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminTest() {
        return "admin secured";
    }
}