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
        GoalTask task = GoalTask.builder()
                .id(7L)
                .title("Test Task")
                .status(TaskStatus.TODO)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 3))
                .build();

        GoalTaskResponse resp = TaskMapper.toResponse(task);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(7L);
        assertThat(resp.getTitle()).isEqualTo("Test Task");
        assertThat(resp.getStatus()).isEqualTo(TaskStatus.TODO);
        assertThat(resp.getStartDate()).isEqualTo(LocalDate.of(2026, 1, 1));
        assertThat(resp.getEndDate()).isEqualTo(LocalDate.of(2026, 1, 3));
    }
}
