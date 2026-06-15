package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.AdminController;
import com.capcom.aspiro.api.dto.response.DataResponse;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.UserResponse;
import com.capcom.aspiro.api.service.interfaces.AdminService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Test
    void getAllUsers_returnsPagedList() {
        AdminController controller = new AdminController(adminService);

        when(adminService.getAllUsers()).thenReturn(List.of(
                UserResponse.builder().id(1L).name("A").email("a@x.com").build(),
                UserResponse.builder().id(2L).name("B").email("b@x.com").build()
        ));

        ResponseEntity<DataResponse<UserResponse>> resp = controller.getUsers();

        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getData()).hasSize(2);
    }

    @Test
    void getUserGoals_existingUser_returnsGoals() {
        AdminController controller = new AdminController(adminService);

        when(adminService.getUserGoals(5L)).thenReturn(List.of(
                GoalResponse.builder().id(10L).title("G1").progress(0).build()
        ));

        ResponseEntity<DataResponse<GoalResponse>> resp = controller.getUserGoals(5L);

        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getData()).hasSize(1);
    }
}
