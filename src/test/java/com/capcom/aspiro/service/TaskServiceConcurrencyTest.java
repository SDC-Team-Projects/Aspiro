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
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceConcurrencyTest {

    @Mock private GoalTaskRepository goalTaskRepository;
    @Mock private GoalStageRepository goalStageRepository;
    @Mock private GoalRepository goalRepository;

    @InjectMocks private TaskServiceImpl taskService;

    @Test
    void concurrentUpdates_toSameTask_finalStateConsistent() throws Exception {
        Goal goal = Goal.builder().id(1L).endDate(LocalDate.now().plusDays(5)).status(GoalStatus.ACTIVE).build();
        GoalStage stage = GoalStage.builder().id(2L).goal(goal).build();
        GoalTask task = GoalTask.builder()
                .id(3L)
                .title("T")
                .status(TaskStatus.TODO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .goalStage(stage)
                .build();

        when(goalTaskRepository.findById(3L)).thenReturn(Optional.of(task));
        when(goalStageRepository.findByGoalId(1L)).thenReturn(List.of(stage));
        when(goalTaskRepository.findByGoalStageId(2L)).thenAnswer(inv -> List.of(task));
        when(goalTaskRepository.save(any(GoalTask.class))).thenAnswer(inv -> inv.getArgument(0));
        when(goalRepository.save(any(Goal.class))).thenAnswer(inv -> inv.getArgument(0));

        UpdateTaskStatusRequest toInProgress = new UpdateTaskStatusRequest();
        toInProgress.setStatus(TaskStatus.IN_PROGRESS);
        UpdateTaskStatusRequest toDone = new UpdateTaskStatusRequest();
        toDone.setStatus(TaskStatus.DONE);

        ExecutorService pool = Executors.newFixedThreadPool(2);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(2);
        AtomicReference<Exception> exRef = new AtomicReference<>(null);

        Runnable r1 = () -> {
            try {
                start.await();
                taskService.updateTaskStatus(3L, toInProgress);
            } catch (Exception e) { exRef.set(e); }
            finally { done.countDown(); }
        };
        Runnable r2 = () -> {
            try {
                start.await();
                taskService.updateTaskStatus(3L, toDone);
            } catch (Exception e) { exRef.set(e); }
            finally { done.countDown(); }
        };

        pool.submit(r1);
        pool.submit(r2);
        start.countDown();
        boolean finished = done.await(3, TimeUnit.SECONDS);
        pool.shutdownNow();

        assertThat(finished).isTrue();
        assertThat(exRef.get()).isNull();
        // Final state should be one of the two statuses, depending on last write
        assertThat(task.getStatus() == TaskStatus.IN_PROGRESS || task.getStatus() == TaskStatus.DONE).isTrue();
        verify(goalRepository, atLeastOnce()).save(any(Goal.class));
    }

    @Test
    void repositorySaveFailure_throws_andNoGoalUpdate() {
        Goal goal = Goal.builder().id(11L).endDate(LocalDate.now().plusDays(1)).status(GoalStatus.ACTIVE).build();
        GoalStage stage = GoalStage.builder().id(21L).goal(goal).build();
        GoalTask task = GoalTask.builder()
                .id(31L)
                .title("T")
                .status(TaskStatus.TODO)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .goalStage(stage)
                .build();

        when(goalTaskRepository.findById(31L)).thenReturn(Optional.of(task));
        when(goalTaskRepository.save(any(GoalTask.class))).thenThrow(new RuntimeException("DB error"));

        UpdateTaskStatusRequest req = new UpdateTaskStatusRequest();
        req.setStatus(TaskStatus.IN_PROGRESS);

        assertThrows(RuntimeException.class, () -> taskService.updateTaskStatus(31L, req));
        // Since saving the task failed, service should not attempt to update goal status
        verify(goalRepository, never()).save(any(Goal.class));
    }
}
