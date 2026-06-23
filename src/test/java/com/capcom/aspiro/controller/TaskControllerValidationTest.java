package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.service.interfaces.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
class TaskControllerValidationTest {

    @TestConfiguration
    static class Cfg {
        @Bean
        @Primary
        TaskService taskService() {
            return Mockito.mock(TaskService.class);
        }
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void updateTaskStatus_invalidStatusValue_returns400() throws Exception {
        String invalidEnumJson = "{ \"status\": \"INVALID\" }";

        mockMvc.perform(patch("/tasks/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidEnumJson))
                .andExpect(status().isBadRequest());
    }
}
