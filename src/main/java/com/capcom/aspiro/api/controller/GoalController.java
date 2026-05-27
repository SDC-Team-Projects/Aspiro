package com.capcom.aspiro.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.service.interfaces.GoalService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/goals")
@RequiredArgsConstructor
public class GoalController {

    private final GoalService goalService;

    @PostMapping
    public ResponseEntity<GoalResponse> createGoal(
            @Valid @RequestBody CreateGoalRequest request,
            Authentication authentication
    ) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(goalService.createGoal(request, authentication.getName()));
    }

    @GetMapping
    public ResponseEntity<List<GoalResponse>> getUserGoals(
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                goalService.getUserGoals(authentication.getName())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalResponse> getGoalById(
            @PathVariable Long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(
                goalService.getGoalById(id, authentication.getName())
        );
    }
}