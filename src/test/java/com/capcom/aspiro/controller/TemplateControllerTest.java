package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.TemplateController;
import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.dto.request.CreateTemplateStageRequest;
import com.capcom.aspiro.api.dto.request.UpdateTemplateRequest;
import com.capcom.aspiro.api.dto.response.TemplateResponse;
import com.capcom.aspiro.api.dto.response.DataResponse;
import com.capcom.aspiro.api.service.interfaces.TemplateService;
import com.capcom.aspiro.api.service.interfaces.TemplateStageService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateControllerTest {

    @Mock
    private TemplateService templateService;

    @Mock
    private TemplateStageService templateStageService;

    @Test
    void getTemplates_returnsEnvelope() {
        TemplateController controller = new TemplateController(templateService, templateStageService);

        when(templateService.getAllTemplates()).thenReturn(List.of(TemplateResponse.builder().id(1L).title("T").description("D").build()));

        ResponseEntity<DataResponse<TemplateResponse>> resp = controller.getTemplates();

        assertThat(resp.getBody().getData()).hasSize(1);
    }

    @Test
    void createTemplate_callsService() {
        TemplateController controller = new TemplateController(templateService, templateStageService);

        CreateTemplateRequest req = new CreateTemplateRequest();
        req.setTitle("X"); req.setDescription("Y");

        when(templateService.createTemplate(req)).thenReturn(TemplateResponse.builder().id(9L).title("X").description("Y").build());

        ResponseEntity<TemplateResponse> resp = controller.createTemplate(req);

        assertThat(resp.getBody().getId()).isEqualTo(9L);
    }

    @Test
    void updateTemplate_success() {
        TemplateController controller = new TemplateController(templateService, templateStageService);

        UpdateTemplateRequest req = new UpdateTemplateRequest();
        req.setTitle("New");
        req.setDescription("Desc");

        when(templateService.updateTemplate(3L, req))
                .thenReturn(TemplateResponse.builder().id(3L).title("New").description("Desc").build());

        ResponseEntity<TemplateResponse> resp = controller.updateTemplate(3L, req);

        assertThat(resp.getBody()).isNotNull();
        assertThat(resp.getBody().getTitle()).isEqualTo("New");
    }

    @Test
    void deleteTemplate_success() {
        TemplateController controller = new TemplateController(templateService, templateStageService);

        ResponseEntity<Void> resp = controller.deleteTemplate(4L);

        assertThat(resp.getStatusCode().is2xxSuccessful()).isTrue();
        verify(templateService, times(1)).deleteTemplate(4L);
    }

    @Test
    void createTemplateStage_success() {
        TemplateController controller = new TemplateController(templateService, templateStageService);

        CreateTemplateStageRequest req = new CreateTemplateStageRequest();
        req.setTitle("Stage 1");
        req.setOrderNumber(1);

        ResponseEntity<Void> resp = controller.createStage(7L, req);

        assertThat(resp.getStatusCode().value()).isEqualTo(201);
        verify(templateStageService, times(1)).createStage(7L, req);
    }
}
