package com.capcom.aspiro.service;

import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.UserResponse;
import com.capcom.aspiro.api.exception.custom.ResourceNotFoundException;
import com.capcom.aspiro.api.service.impl.AdminServiceImpl;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;
import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.GoalRepository;
import com.capcom.aspiro.domain.repository.GoalStageRepository;
import com.capcom.aspiro.domain.repository.GoalTaskRepository;
import com.capcom.aspiro.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalStageRepository goalStageRepository;

    @Mock
    private GoalTaskRepository goalTaskRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @Test
    void getAllUsers_returnsUserResponses() {
        User u = User.builder().id(1L).name("A").email("a@x.com").role(UserRole.USER).build();
        when(userRepository.findAll()).thenReturn(List.of(u));

        List<UserResponse> res = adminService.getAllUsers();

        assertThat(res).hasSize(1);
        assertThat(res.get(0).getEmail()).isEqualTo("a@x.com");
    }

    @Test
    void getUserGoals_userNotFound_throws() {
        when(userRepository.findById(10L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> adminService.getUserGoals(10L));
    }

    @Test
    void getUserGoals_returnsGoalsWithProgress() {
        User u = User.builder().id(2L).name("B").email("b@x.com").role(UserRole.USER).build();
        Goal g = Goal.builder().id(11L).title("G").build();
        GoalStage s = GoalStage.builder().id(21L).title("S").build();
        GoalTask t1 = GoalTask.builder().id(31L).status(TaskStatus.DONE).build();
        GoalTask t2 = GoalTask.builder().id(32L).status(TaskStatus.TODO).build();

        when(userRepository.findById(2L)).thenReturn(Optional.of(u));
        when(goalRepository.findByUserId(2L)).thenReturn(List.of(g));
        when(goalStageRepository.findByGoalId(11L)).thenReturn(List.of(s));
        when(goalTaskRepository.findByGoalStageId(21L)).thenReturn(List.of(t1, t2));

        List<GoalResponse> goals = adminService.getUserGoals(2L);

        assertThat(goals).hasSize(1);
        GoalResponse gr = goals.get(0);
        assertThat(gr.getProgress()).isEqualTo(50);
    }
}
