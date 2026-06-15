package com.capcom.aspiro.repository;

import com.capcom.aspiro.domain.model.*;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class GoalRepositoriesDataJpaTest {

    @Autowired private UserRepository userRepository;
    @Autowired private TemplateRepository templateRepository;
    @Autowired private GoalRepository goalRepository;
    @Autowired private GoalStageRepository goalStageRepository;
    @Autowired private GoalTaskRepository goalTaskRepository;
    @Autowired private TemplateStageRepository templateStageRepository;
    @Autowired private TemplateTaskRepository templateTaskRepository;

    @Test
    void saveAndFind_Goal_withStagesAndTasks_andCustomFinders() {
        // Create user and template
        User user = User.builder()
                .name("U")
                .email("u@example.com")
                .password("p")
                .role(UserRole.USER)
                .build();
        user = userRepository.save(user);

        Template template = Template.builder()
                .title("T")
                .description("D")
                .build();
        template = templateRepository.save(template);

        // Create goal
        Goal goal = Goal.builder()
                .title("G")
                .startDate(LocalDate.now())
                .status(GoalStatus.ACTIVE)
                .user(user)
                .template(template)
                .build();
        goal = goalRepository.save(goal);

        // Create template stage and template tasks to satisfy not-null FKs
        TemplateStage tStage = TemplateStage.builder()
                .title("TS1")
                .orderNumber(1)
                .template(template)
                .build();
        tStage = templateStageRepository.save(tStage);

        TemplateTask tt1 = TemplateTask.builder()
                .title("TT1")
                .description("D1")
                .orderNumber(1)
                .daysOffset(0)
                .durationDays(1)
                .templateStage(tStage)
                .build();
        TemplateTask tt2 = TemplateTask.builder()
                .title("TT2")
                .description("D2")
                .orderNumber(2)
                .daysOffset(1)
                .durationDays(1)
                .templateStage(tStage)
                .build();
        tt1 = templateTaskRepository.save(tt1);
        tt2 = templateTaskRepository.save(tt2);

        // Create stage and tasks linked to template entities
        GoalStage stage = GoalStage.builder()
                .title("S1")
                .orderNumber(1)
                .goal(goal)
                .templateStage(tStage)
                .build();
        stage = goalStageRepository.save(stage);

        GoalTask t1 = GoalTask.builder()
                .title("Task 1")
                .status(TaskStatus.TODO)
                .orderNumber(1)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(1))
                .goalStage(stage)
                .templateTask(tt1)
                .build();
        GoalTask t2 = GoalTask.builder()
                .title("Task 2")
                .status(TaskStatus.DONE)
                .orderNumber(2)
                .startDate(LocalDate.now())
                .endDate(LocalDate.now().plusDays(2))
                .goalStage(stage)
                .templateTask(tt2)
                .build();
        goalTaskRepository.saveAll(List.of(t1, t2));

        // Verify custom finders
        List<GoalStage> stages = goalStageRepository.findByGoalId(goal.getId());
        assertThat(stages).hasSize(1);
        assertThat(stages.get(0).getTitle()).isEqualTo("S1");

        List<GoalTask> tasks = goalTaskRepository.findByGoalStageId(stage.getId());
        assertThat(tasks).hasSize(2);
        assertThat(tasks.stream().map(GoalTask::getOrderNumber)).containsExactlyInAnyOrder(1, 2);

        // Verify GoalRepository.findByUserId
        List<Goal> byUserId = goalRepository.findByUserId(user.getId());
        assertThat(byUserId).extracting(Goal::getTitle).contains("G");
    }
}
