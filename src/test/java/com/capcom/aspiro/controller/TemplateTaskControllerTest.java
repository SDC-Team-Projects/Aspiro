package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.TemplateTaskController;
import com.capcom.aspiro.api.dto.request.UpdateTemplateTaskRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateTaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class TemplateTaskControllerTest {

    @Mock
    private TemplateTaskService templateTaskService;

    @Test
    void updateTask_success() {
        TemplateTaskController controller = new TemplateTaskController(templateTaskService);

        UpdateTemplateTaskRequest req = new UpdateTemplateTaskRequest();
        req.setTitle("Task X");
        req.setOrderNumber(3);
        req.setDaysOffset(2);
        req.setDurationDays(4);

        ResponseEntity<Void> resp = controller.updateTask(15L, req);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        verify(templateTaskService, times(1)).updateTask(15L, req);
    }

    @Test
    void deleteTask_success() {
        TemplateTaskController controller = new TemplateTaskController(templateTaskService);

        ResponseEntity<Void> resp = controller.deleteTask(16L);

        assertThat(resp.getStatusCode().value()).isEqualTo(204);
        verify(templateTaskService, times(1)).deleteTask(16L);
    }
}
