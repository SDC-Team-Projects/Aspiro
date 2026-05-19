package com.capcom.aspiro;

import com.capcom.aspiro.domain.model.*;
import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import com.capcom.aspiro.domain.model.enums.UserRole;
import com.capcom.aspiro.domain.repository.*;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PersistenceIntegrationTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TemplateRepository templateRepository;

    @Autowired
    private TemplateStageRepository templateStageRepository;

    @Autowired
    private TemplateTaskRepository templateTaskRepository;

    @Autowired
    private GoalRepository goalRepository;

    @Autowired
    private GoalStageRepository goalStageRepository;

    @Autowired
    private GoalTaskRepository goalTaskRepository;

    @Test
    void shouldSaveFullGoalStructureToDatabase() {
        User user = User.builder()
                .name("Test User")
                .email("test@example.com")
                .password("password")
                .role(UserRole.USER)
                .build();

        user = userRepository.save(user);

        Template template = Template.builder()
                .title("Java Learning Template")
                .description("Template for learning Java")
                .build();

        template = templateRepository.save(template);

        TemplateStage templateStage = TemplateStage.builder()
                .title("Java Basics")
                .orderNumber(1)
                .template(template)
                .build();

        templateStage = templateStageRepository.save(templateStage);

        TemplateTask templateTask = TemplateTask.builder()
                .title("Learn variables")
                .description("Study Java variables")
                .orderNumber(1)
                .daysOffset(0)
                .durationDays(2)
                .templateStage(templateStage)
                .build();

        templateTask = templateTaskRepository.save(templateTask);

        Goal goal = Goal.builder()
                .title("Learn Java")
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 10))
                .status(GoalStatus.ACTIVE)
                .user(user)
                .template(template)
                .build();

        goal = goalRepository.save(goal);

        GoalStage goalStage = GoalStage.builder()
                .title("Java Basics")
                .orderNumber(1)
                .goal(goal)
                .templateStage(templateStage)
                .build();

        goalStage = goalStageRepository.save(goalStage);

        GoalTask goalTask = GoalTask.builder()
                .title("Learn variables")
                .description("Study Java variables")
                .status(TaskStatus.TODO)
                .startDate(LocalDate.of(2026, 1, 1))
                .endDate(LocalDate.of(2026, 1, 3))
                .orderNumber(1)
                .goalStage(goalStage)
                .templateTask(templateTask)
                .build();

        goalTask = goalTaskRepository.save(goalTask);

        assertThat(user.getId()).isNotNull();
        assertThat(template.getId()).isNotNull();
        assertThat(goal.getId()).isNotNull();
        assertThat(goalTask.getId()).isNotNull();

        assertThat(userRepository.existsByEmail("test@example.com")).isTrue();
        assertThat(userRepository.findByEmail("test@example.com")).isPresent();

        assertThat(goalRepository.findByUserId(user.getId())).hasSize(1);
        assertThat(goalStageRepository.findByGoalId(goal.getId())).hasSize(1);
        assertThat(goalTaskRepository.findByGoalStageId(goalStage.getId())).hasSize(1);
        assertThat(goalTaskRepository.findByStatus(TaskStatus.TODO)).hasSize(1);
    }
}