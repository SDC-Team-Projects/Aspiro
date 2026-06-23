package com.capcom.aspiro.service;

import com.capcom.aspiro.api.dto.request.UpdateTaskStatusRequest;
import com.capcom.aspiro.api.service.impl.TaskServiceImpl;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;
import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.repository.GoalRepository;
import com.capcom.aspiro.domain.repository.GoalStageRepository;
import com.capcom.aspiro.domain.repository.GoalTaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private GoalTaskRepository goalTaskRepository;

    @Mock
    private GoalStageRepository goalStageRepository;

    @Mock
    private GoalRepository goalRepository;

    @InjectMocks
    private TaskServiceImpl taskService;

    @Test
    void updateTaskStatus_nonExisting_throws() {
        when(goalTaskRepository.findById(5L)).thenReturn(Optional.empty());

        UpdateTaskStatusRequest req = new UpdateTaskStatusRequest();
        req.setStatus(TaskStatus.DONE);

        assertThrows(RuntimeException.class, () -> taskService.updateTaskStatus(5L, req));
    }

    @Test
    void updateTaskStatus_setsOverdue_ifPastEndDate_andNotDone() {
        Goal goal = Goal.builder().id(10L).endDate(LocalDate.now().minusDays(1)).status(GoalStatus.ACTIVE).build();
        GoalStage stage = GoalStage.builder().id(20L).goal(goal).build();

        GoalTask task = GoalTask.builder().id(30L).title("T").status(TaskStatus.TODO).endDate(LocalDate.now().minusDays(2)).goalStage(stage).build();

        when(goalTaskRepository.findById(30L)).thenReturn(Optional.of(task));
        when(goalStageRepository.findByGoalId(10L)).thenReturn(List.of(stage));
        when(goalTaskRepository.findByGoalStageId(20L)).thenReturn(List.of(task));
        when(goalTaskRepository.save(any(GoalTask.class))).thenAnswer(i -> i.getArgument(0));
        when(goalRepository.save(any(Goal.class))).thenAnswer(i -> i.getArgument(0));

        UpdateTaskStatusRequest req = new UpdateTaskStatusRequest();
        req.setStatus(TaskStatus.IN_PROGRESS);

        var resp = taskService.updateTaskStatus(30L, req);

        assertThat(resp.getStatus()).isEqualTo(TaskStatus.OVERDUE);
        verify(goalRepository, times(1)).save(any(Goal.class));
    }

    @Test
    void updateTaskStatus_marksGoalCompleted_whenAllTasksDone() {
        Goal goal = Goal.builder().id(11L).endDate(LocalDate.now().plusDays(5)).status(GoalStatus.ACTIVE).build();
        GoalStage stage = GoalStage.builder().id(21L).goal(goal).build();

        GoalTask t1 = GoalTask.builder().id(31L).status(TaskStatus.IN_PROGRESS).endDate(LocalDate.now().plusDays(1)).goalStage(stage).build();
        GoalTask t2 = GoalTask.builder().id(32L).status(TaskStatus.DONE).endDate(LocalDate.now().plusDays(1)).goalStage(stage).build();

        when(goalTaskRepository.findById(31L)).thenReturn(Optional.of(t1));
        when(goalStageRepository.findByGoalId(11L)).thenReturn(List.of(stage));
        when(goalTaskRepository.findByGoalStageId(21L)).thenReturn(List.of(t1, t2));
        when(goalTaskRepository.save(any(GoalTask.class))).thenAnswer(i -> i.getArgument(0));
        when(goalRepository.save(any(Goal.class))).thenAnswer(i -> i.getArgument(0));

        UpdateTaskStatusRequest req = new UpdateTaskStatusRequest();
        req.setStatus(TaskStatus.DONE);

        var resp = taskService.updateTaskStatus(31L, req);

        assertThat(resp.getStatus()).isEqualTo(TaskStatus.DONE);
        verify(goalRepository, times(1)).save(any(Goal.class));
    }
}
