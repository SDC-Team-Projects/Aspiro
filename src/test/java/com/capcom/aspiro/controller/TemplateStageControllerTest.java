package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.TemplateStageController;
import com.capcom.aspiro.api.dto.request.CreateTemplateTaskRequest;
import com.capcom.aspiro.api.dto.request.UpdateTemplateStageRequest;
import com.capcom.aspiro.api.service.interfaces.TemplateStageService;
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
class TemplateStageControllerTest {

    @Mock
    private TemplateStageService templateStageService;

    @Mock
    private TemplateTaskService templateTaskService;

    @Test
    void createTask_forStage_success() {
        TemplateStageController controller = new TemplateStageController(templateStageService, templateTaskService);

        CreateTemplateTaskRequest req = new CreateTemplateTaskRequest();
        req.setTitle("Task 1");
        req.setOrderNumber(1);
        req.setDaysOffset(0);
        req.setDurationDays(1);

        ResponseEntity<Void> resp = controller.createTask(11L, req);

        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        verify(templateTaskService, times(1)).createTask(11L, req);
    }

    @Test
    void updateStage_success() {
        TemplateStageController controller = new TemplateStageController(templateStageService, templateTaskService);

        UpdateTemplateStageRequest req = new UpdateTemplateStageRequest();
        req.setTitle("Updated");
        req.setOrderNumber(2);

        ResponseEntity<Void> resp = controller.updateStage(5L, req);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        verify(templateStageService, times(1)).updateStage(5L, req);
    }

    @Test
    void deleteStage_success() {
        TemplateStageController controller = new TemplateStageController(templateStageService, templateTaskService);

        ResponseEntity<Void> resp = controller.deleteStage(9L);

        assertThat(resp.getStatusCode().value()).isEqualTo(204);
        verify(templateStageService, times(1)).deleteStage(9L);
    }
}
