package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.dto.request.CreateTemplateStageRequest;
import com.capcom.aspiro.api.dto.request.CreateTemplateTaskRequest;
import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class ValidationControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void createTemplate_missingTitle_badRequest() throws Exception {
        CreateTemplateRequest req = new CreateTemplateRequest();
        req.setTitle("");

        mockMvc.perform(post("/templates")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTemplateStage_missingOrderNumber_badRequest() throws Exception {
        CreateTemplateStageRequest req = new CreateTemplateStageRequest();
        req.setTitle("Stage");
        req.setOrderNumber(null);

        mockMvc.perform(post("/templates/1/stages")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createTemplateTask_missingDaysOffset_badRequest() throws Exception {
        CreateTemplateTaskRequest req = new CreateTemplateTaskRequest();
        req.setTitle("Task");
        req.setOrderNumber(1);
        req.setDaysOffset(null);
        req.setDurationDays(1);

        mockMvc.perform(post("/template-stages/1/tasks")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void createGoal_missingStartDate_badRequest() throws Exception {
        CreateGoalRequest req = new CreateGoalRequest();
        req.setTemplateId(1L);
        req.setTitle("G");
        req.setStartDate(null);

        mockMvc.perform(post("/goals")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest());
    }
}
