package com.capcom.aspiro.api.dto.response;

import com.capcom.aspiro.domain.model.enums.UserRole;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserProfileResponse {

    private Long id;

    private String name;

    private String email;

    private UserRole role;
}