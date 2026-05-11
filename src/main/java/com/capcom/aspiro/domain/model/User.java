package com.capcom.aspiro.domain.model;

import java.util.ArrayList;
import java.util.List;

import com.capcom.aspiro.domain.model.enums.UserRole;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private Long id;
    private String name;
    private String email;
    private String password;
    private UserRole role;

    @Builder.Default
    private List<Goal> goals = new ArrayList<>();
}
