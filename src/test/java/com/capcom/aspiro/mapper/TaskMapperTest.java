package com.capcom.aspiro.mapper;

import com.capcom.aspiro.api.dto.response.GoalTaskResponse;
import com.capcom.aspiro.api.mapper.TaskMapper;
import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TaskMapperTest {

    @Test
    void toResponse_mapsFields() {
        LocalDate startDate = LocalDate.now().plusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(3);

        GoalTask task = GoalTask.builder()
                .id(7L)
                .title("Test Task")
                .status(TaskStatus.TODO)
                .startDate(startDate)
                .endDate(endDate)
                .build();

        GoalTaskResponse resp = TaskMapper.toResponse(task);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(7L);
        assertThat(resp.getTitle()).isEqualTo("Test Task");
        assertThat(resp.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(resp.getStartDate()).isEqualTo(startDate);
        assertThat(resp.getEndDate()).isEqualTo(endDate);
    }

    @Test
    void toResponse_returnsOverdueWhenTaskIsPastAndNotDone() {
        GoalTask task = GoalTask.builder()
                .id(8L)
                .title("Overdue Task")
                .status(TaskStatus.TODO)
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().minusDays(1))
                .build();

        GoalTaskResponse resp = TaskMapper.toResponse(task);

        assertThat(resp).isNotNull();
        assertThat(resp.getStatus()).isEqualTo(TaskStatus.OVERDUE);
    }

    @Test
    void toResponse_keepsDoneStatusEvenWhenTaskIsPast() {
        GoalTask task = GoalTask.builder()
                .id(9L)
                .title("Done Task")
                .status(TaskStatus.DONE)
                .startDate(LocalDate.now().minusDays(3))
                .endDate(LocalDate.now().minusDays(1))
                .build();

        GoalTaskResponse resp = TaskMapper.toResponse(task);

        assertThat(resp).isNotNull();
        assertThat(resp.getStatus()).isEqualTo(TaskStatus.DONE);
    }
}