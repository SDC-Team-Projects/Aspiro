package com.capcom.aspiro.integration;

import com.capcom.aspiro.AspiroApplication;
import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.dto.request.LoginRequest;
import com.capcom.aspiro.api.dto.request.RegisterRequest;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = AspiroApplication.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class FullFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @BeforeEach
    void setUp() {
        // clean DB if necessary (H2 create-drop handles schema)
    }

    @Test
    void fullUserFlow_register_login_createTemplate_createGoal_and_getAnalytics() throws Exception {
        RegisterRequest reg = new RegisterRequest();
        reg.setName("Integration User");
        reg.setEmail("int@example.com");
        reg.setPassword("secret123");

        mockMvc.perform(post("/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(reg)))
                .andExpect(status().isCreated());

        LoginRequest login = new LoginRequest();
        login.setEmail("int@example.com");
        login.setPassword("secret123");

        String loginBody = mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andReturn().getResponse().getContentAsString();

        Map<String, Object> loginResp = mapper.readValue(loginBody, Map.class);
        assertThat(loginResp).containsKey("accessToken");
        String token = (String) loginResp.get("accessToken");

        // make the user admin directly in DB for this test
        userRepository.findByEmail("int@example.com").ifPresent(u -> { u.setRole(UserRole.ADMIN); userRepository.save(u); });

        CreateTemplateRequest tpl = new CreateTemplateRequest();
        tpl.setTitle("Int Template");
        tpl.setDescription("desc");

        String templateBody = mockMvc.perform(post("/templates")
                .header("Authorization", "Bearer " + token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(tpl)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Map<String,Object> templateResp = mapper.readValue(templateBody, Map.class);
        assertThat(templateResp).containsKey("id");
        Integer templateId = (Integer) templateResp.get("id");

        CreateGoalRequest cg = new CreateGoalRequest();
        cg.setTemplateId(templateId.longValue());
        cg.setTitle("My Goal");
        cg.setStartDate(LocalDate.now());

        Principal principal = new UsernamePasswordAuthenticationToken("int@example.com", null);

        String goalBody = mockMvc.perform(post("/goals")
                .principal(principal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(cg)))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();

        Map<String,Object> goalResp = mapper.readValue(goalBody, Map.class);
        assertThat(goalResp).containsKey("id");

        String analyticsBody = mockMvc.perform(get("/analytics")
                .principal(principal)
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        Map<String,Object> analytics = mapper.readValue(analyticsBody, Map.class);
        assertThat(analytics).containsKeys("goalProgress", "tasksDone", "totalTasks");
    }
}
