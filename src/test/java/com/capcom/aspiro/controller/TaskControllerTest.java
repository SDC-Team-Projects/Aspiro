package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.TaskController;
import com.capcom.aspiro.api.dto.request.UpdateTaskStatusRequest;
import com.capcom.aspiro.api.dto.response.GoalTaskResponse;
import com.capcom.aspiro.api.service.interfaces.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import com.capcom.aspiro.domain.model.enums.TaskStatus;

import static org.assertj.core.api.Assertions.assertThat;
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

        when(taskService.updateTaskStatus(1L, req)).thenReturn(GoalTaskResponse.builder().id(1L).title("T").status(TaskStatus.DONE).build());

        ResponseEntity<GoalTaskResponse> resp = controller.updateTaskStatus(1L, req);

        assertThat(resp.getBody().getStatus()).isEqualTo(TaskStatus.DONE);
    }
}
