package com.capcom.aspiro.api.controller;

import com.capcom.aspiro.api.service.interfaces.AdminService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.UserResponse;
import com.capcom.aspiro.api.dto.response.DataResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/users")
    public ResponseEntity<DataResponse<UserResponse>> getUsers() {

        return ResponseEntity.ok(
                DataResponse.of(adminService.getAllUsers())
        );
    }

    @GetMapping("/users/{id}/goals")
    public ResponseEntity<DataResponse<GoalResponse>> getUserGoals(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                DataResponse.of(adminService.getUserGoals(id))
        );
    }
}