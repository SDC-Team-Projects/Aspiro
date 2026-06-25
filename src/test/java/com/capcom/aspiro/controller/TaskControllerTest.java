package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.TaskController;
import com.capcom.aspiro.api.dto.request.UpdateTaskStatusRequest;
import com.capcom.aspiro.api.dto.response.GoalTaskResponse;
import com.capcom.aspiro.api.service.interfaces.TaskService;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

    @Mock
    private TaskService taskService;

    @Test
    void updateTaskStatus_returnsTaskResponse() {
        TaskController controller = new TaskController(taskService);

        UpdateTaskStatusRequest req = new UpdateTaskStatusRequest();
        req.setStatus(TaskStatus.DONE);

        Authentication authentication = mock(Authentication.class);

        when(authentication.getName())
                .thenReturn("user@example.com");

        GoalTaskResponse response = GoalTaskResponse.builder()
                .id(1L)
                .title("T")
                .status(TaskStatus.DONE)
                .build();

        when(taskService.updateTaskStatus(1L, req, "user@example.com"))
                .thenReturn(response);

        ResponseEntity<GoalTaskResponse> resp = controller.updateTaskStatus(
                1L,
                req,
                authentication
        );

        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getStatus()).isEqualTo(TaskStatus.DONE);
    }
}