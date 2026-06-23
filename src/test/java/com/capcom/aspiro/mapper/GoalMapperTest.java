package com.capcom.aspiro.mapper;

import com.capcom.aspiro.api.dto.response.GoalResponse;
import com.capcom.aspiro.api.dto.response.GoalStageResponse;
import com.capcom.aspiro.api.mapper.GoalMapper;
import com.capcom.aspiro.domain.model.Goal;
import com.capcom.aspiro.domain.model.GoalStage;
import com.capcom.aspiro.domain.model.GoalTask;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class GoalMapperTest {

    @Test
    void toSummaryResponse_withoutStages_mapsFields() {
        Goal goal = Goal.builder().id(1L).title("G").status(null).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).build();

        GoalResponse resp = GoalMapper.toSummaryResponse(goal, 42);

        assertThat(resp).isNotNull();
        assertThat(resp.getId()).isEqualTo(1L);
        assertThat(resp.getTitle()).isEqualTo("G");
        assertThat(resp.getProgress()).isEqualTo(42);
    }

    @Test
    void toDetailedResponse_withStages_mapsStagesAndTasks() {
        Goal goal = Goal.builder().id(2L).title("G2").status(null).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(5)).build();

        GoalTask t = GoalTask.builder().id(10L).title("Task1").status(null).startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1)).build();

        GoalStage s = GoalStage.builder().id(5L).title("Stage").orderNumber(1).goalTasks(List.of(t)).build();

        var detailed = GoalMapper.toDetailedResponse(goal, 10, List.of(s));

        assertThat(detailed).isNotNull();
        assertThat(detailed.getStages()).hasSize(1);
        GoalStageResponse sr = detailed.getStages().get(0);
        assertThat(sr.getTasks()).hasSize(1);
    }
}
