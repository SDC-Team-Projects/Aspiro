package com.capcom.aspiro.service;

import com.capcom.aspiro.api.service.impl.AnalyticsServiceImpl;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;
import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.User;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.GoalRepository;
import com.capcom.aspiro.domain.repository.GoalStageRepository;
import com.capcom.aspiro.domain.repository.GoalTaskRepository;
import com.capcom.aspiro.domain.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AnalyticsServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalStageRepository goalStageRepository;

    @Mock
    private GoalTaskRepository goalTaskRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @Test
    void getUserAnalytics_countsCorrectly() {
        User u = User.builder().id(1L).email("a@x.com").build();
        Goal g = Goal.builder().id(2L).status(null).build();
        GoalStage s = GoalStage.builder().id(3L).build();
        GoalTask t1 = GoalTask.builder().id(4L).status(TaskStatus.DONE).endDate(LocalDate.now().minusDays(1)).build();
        GoalTask t2 = GoalTask.builder().id(5L).status(TaskStatus.TODO).endDate(LocalDate.now().minusDays(2)).build();

        when(userRepository.findByEmail("a@x.com")).thenReturn(Optional.of(u));
        when(goalRepository.findByUserId(1L)).thenReturn(List.of(g));
        when(goalStageRepository.findByGoalId(2L)).thenReturn(List.of(s));
        when(goalTaskRepository.findByGoalStageId(3L)).thenReturn(List.of(t1, t2));

        var res = analyticsService.getUserAnalytics("a@x.com");

        assertThat(res.getTotalTasks()).isEqualTo(2);
        assertThat(res.getTasksDone()).isEqualTo(1);
        assertThat(res.getOverdueTasks()).isEqualTo(1);
    }
}
