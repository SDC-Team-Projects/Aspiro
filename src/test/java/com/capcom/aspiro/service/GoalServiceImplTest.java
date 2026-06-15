package com.capcom.aspiro.service;

import com.capcom.aspiro.api.dto.request.CreateGoalRequest;
import com.capcom.aspiro.api.service.impl.GoalServiceImpl;
import com.capcom.aspiro.domain.model.*;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GoalServiceImplTest {

    @Mock
    private GoalRepository goalRepository;

    @Mock
    private GoalStageRepository goalStageRepository;

    @Mock
    private GoalTaskRepository goalTaskRepository;

    @Mock
    private TemplateRepository templateRepository;

    @Mock
    private TemplateStageRepository templateStageRepository;

    @Mock
    private TemplateTaskRepository templateTaskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private GoalServiceImpl goalService;

    @Test
    void calculateGoalProgress_noTasks_returnsZero() throws Exception {
        Goal goal = Goal.builder().id(1L).build();
        when(goalStageRepository.findByGoalId(1L)).thenReturn(List.of());

        Method m = GoalServiceImpl.class.getDeclaredMethod("calculateGoalProgress", Goal.class);
        m.setAccessible(true);
        int progress = (int) m.invoke(goalService, goal);

        assertThat(progress).isEqualTo(0);
    }

    @Test
    void calculateGoalProgress_allDone_returns100() throws Exception {
        Goal goal = Goal.builder().id(2L).build();
        GoalStage s = GoalStage.builder().id(5L).build();
        GoalTask t1 = GoalTask.builder().id(10L).status(TaskStatus.DONE).build();
        GoalTask t2 = GoalTask.builder().id(11L).status(TaskStatus.DONE).build();

        when(goalStageRepository.findByGoalId(2L)).thenReturn(List.of(s));
        when(goalTaskRepository.findByGoalStageId(5L)).thenReturn(List.of(t1, t2));

        Method m = GoalServiceImpl.class.getDeclaredMethod("calculateGoalProgress", Goal.class);
        m.setAccessible(true);
        int progress = (int) m.invoke(goalService, goal);

        assertThat(progress).isEqualTo(100);
    }

    @Test
    void createGoal_missingTemplate_throws() {
        CreateGoalRequest req = new CreateGoalRequest();
        req.setTemplateId(999L);
        req.setTitle("X");
        req.setStartDate(LocalDate.now());

        when(userRepository.findByEmail("u@x.com")).thenReturn(Optional.of(User.builder().id(1L).email("u@x.com").build()));
        when(templateRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> goalService.createGoal(req, "u@x.com"));

        verify(goalRepository, never()).save(any(Goal.class));
    }
    @Test
    void calculateGoalProgress_some_done_roundsDown() throws Exception {
        Goal goal = Goal.builder().id(3L).build();
        GoalStage s = GoalStage.builder().id(6L).build();

        GoalTask t1 = GoalTask.builder().id(20L).status(TaskStatus.DONE).build();
        GoalTask t2 = GoalTask.builder().id(21L).status(TaskStatus.DONE).build();
        GoalTask t3 = GoalTask.builder().id(22L).status(TaskStatus.TODO).build();

        when(goalStageRepository.findByGoalId(3L)).thenReturn(List.of(s));
        when(goalTaskRepository.findByGoalStageId(6L)).thenReturn(List.of(t1, t2, t3));

        var m = GoalServiceImpl.class.getDeclaredMethod("calculateGoalProgress", Goal.class);
        m.setAccessible(true);
        int progress = (int) m.invoke(goalService, goal);

        // 2/3 = 66 (integer math should round down)
        assertThat(progress).isEqualTo(66);
    }

    @Test
    void createGoal_success_copiesTemplate_and_setsEndDateToLatest() {
        // Arrange user and template
        var userEmail = "u@x.com";
        User user = User.builder().id(100L).email(userEmail).build();
        Template template = Template.builder().id(200L).title("T").build();

        when(userRepository.findByEmail(userEmail)).thenReturn(Optional.of(user));
        when(templateRepository.findById(200L)).thenReturn(Optional.of(template));

        // Goal save: first call assigns ID, second just returns goal
        when(goalRepository.save(any(Goal.class))).thenAnswer(inv -> {
            Goal g = inv.getArgument(0);
            if (g.getId() == null) {
                g.setId(300L);
            }
            return g;
        });

        // Template stages in order
        TemplateStage ts1 = TemplateStage.builder().id(400L).title("S1").orderNumber(1).template(template).build();
        TemplateStage ts2 = TemplateStage.builder().id(401L).title("S2").orderNumber(2).template(template).build();
        when(templateStageRepository.findByTemplateIdOrderByOrderNumber(200L)).thenReturn(List.of(ts1, ts2));

        // Saving goal stages assigns ids
        when(goalStageRepository.save(any(GoalStage.class))).thenAnswer(inv -> {
            GoalStage s = inv.getArgument(0);
            if (s.getId() == null) s.setId(500L + (long) s.getOrderNumber());
            return s;
        });

        // Template tasks per stage
        // Start date will be 2024-01-01; latest end expected: start + 5 days offset + 3 duration = 2024-01-09
        TemplateTask tt11 = TemplateTask.builder().id(600L).title("A").orderNumber(1).daysOffset(0).durationDays(1).templateStage(ts1).build();
        TemplateTask tt21 = TemplateTask.builder().id(601L).title("B").orderNumber(1).daysOffset(5).durationDays(3).templateStage(ts2).build();
        when(templateTaskRepository.findByTemplateStageIdOrderByOrderNumber(400L)).thenReturn(List.of(tt11));
        when(templateTaskRepository.findByTemplateStageIdOrderByOrderNumber(401L)).thenReturn(List.of(tt21));

        // Stubbing for progress calculation during mapping
        when(goalStageRepository.findByGoalId(300L)).thenReturn(List.of());

        // Act
        CreateGoalRequest req = new CreateGoalRequest();
        req.setTemplateId(200L);
        req.setTitle("G");
        req.setStartDate(LocalDate.of(2024, 1, 1));

        var summary = goalService.createGoal(req, userEmail);

        // Assert
        assertThat(summary.getTitle()).isEqualTo("G");

        // Verify that the goal's endDate was set to 2024-01-09 on the second save
        var captor = org.mockito.ArgumentCaptor.forClass(Goal.class);
        verify(goalRepository, atLeast(2)).save(captor.capture());
        List<Goal> saved = captor.getAllValues();
        Goal lastSaved = saved.get(saved.size() - 1);
        assertThat(lastSaved.getEndDate()).isEqualTo(LocalDate.of(2024, 1, 9));
    }
}
