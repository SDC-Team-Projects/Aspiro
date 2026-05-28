package com.capcom.aspiro.api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.GoalDetailedResponse;
import com.capcom.aspiro.api.dto.response.DataResponse;
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
            @Valid @RequestBody CreateGoalRequest request
    ) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(goalService.createGoal(request));
    }

    @GetMapping
    public ResponseEntity<DataResponse<GoalResponse>> getUserGoals() {

        return ResponseEntity.ok(
                DataResponse.of(goalService.getUserGoals())
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<GoalDetailedResponse> getGoalById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                goalService.getGoalById(id)
        );
    }
}