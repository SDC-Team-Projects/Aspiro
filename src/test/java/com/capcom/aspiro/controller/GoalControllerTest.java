package com.capcom.aspiro.controller;

import com.capcom.aspiro.api.controller.GoalController;
import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.GoalDetailedResponse;
import com.capcom.aspiro.api.service.interfaces.GoalService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GoalControllerTest {

    @Mock
    private GoalService goalService;

    @Mock
    private Authentication authentication;

    @Test
    void getUserGoals_returnsList() {
        GoalController controller = new GoalController(goalService);

        when(authentication.getName()).thenReturn("user@example.com");
        when(goalService.getUserGoals("user@example.com")).thenReturn(List.of(GoalResponse.builder().id(1L).title("G").progress(10).build()));

        ResponseEntity<List<GoalResponse>> resp = controller.getUserGoals(authentication);

        assertThat(resp.getBody()).hasSize(1);
    }

    @Test
    void getGoalById_returnsDetailed() {
        GoalController controller = new GoalController(goalService);

        when(authentication.getName()).thenReturn("user@example.com");
        when(goalService.getGoalById(1L, "user@example.com")).thenReturn(GoalDetailedResponse.builder().id(1L).title("G").build());

        ResponseEntity<GoalDetailedResponse> resp = controller.getGoalById(1L, authentication);

        assertThat(resp.getBody().getId()).isEqualTo(1L);
    }
}
