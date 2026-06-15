package com.capcom.aspiro.repository;

import com.capcom.aspiro.domain.model.Template;
import com.capcom.aspiro.domain.model.TemplateStage;
import com.capcom.aspiro.domain.model.TemplateTask;
import com.capcom.aspiro.domain.repository.TemplateRepository;
import com.capcom.aspiro.domain.repository.TemplateStageRepository;
import com.capcom.aspiro.domain.repository.TemplateTaskRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class TemplateRepositoriesDataJpaTest {

    @Autowired
    private TemplateRepository templateRepository;
    @Autowired
    private TemplateStageRepository templateStageRepository;
    @Autowired
    private TemplateTaskRepository templateTaskRepository;

    @Test
    void saveAndFind_Template_withStagesAndTasks_andCustomFinders() {
        // Create template
        Template template = Template.builder()
                .title("Template A")
                .description("Desc")
                .build();
        template = templateRepository.save(template);

        // Create two stages with different orderNumbers
        TemplateStage s1 = TemplateStage.builder()
                .title("Stage 1")
                .orderNumber(2)
                .template(template)
                .build();
        TemplateStage s2 = TemplateStage.builder()
                .title("Stage 0")
                .orderNumber(1)
                .template(template)
                .build();
        s1 = templateStageRepository.save(s1);
        s2 = templateStageRepository.save(s2);

        // Tasks for stage s2 (order 1)
        TemplateTask t21 = TemplateTask.builder()
                .title("Task A")
                .description("DA")
                .orderNumber(2)
                .daysOffset(1)
                .durationDays(2)
                .templateStage(s2)
                .build();
        TemplateTask t22 = TemplateTask.builder()
                .title("Task B")
                .description("DB")
                .orderNumber(1)
                .daysOffset(0)
                .durationDays(1)
                .templateStage(s2)
                .build();
        templateTaskRepository.saveAll(List.of(t21, t22));

        // Verify custom stage finder ordering
        List<TemplateStage> stages = templateStageRepository
                .findByTemplateIdOrderByOrderNumber(template.getId());
        assertThat(stages).hasSize(2);
        assertThat(stages.get(0).getOrderNumber()).isEqualTo(1);
        assertThat(stages.get(1).getOrderNumber()).isEqualTo(2);

        // Verify custom task finder ordering
        List<TemplateTask> tasks = templateTaskRepository
                .findByTemplateStageIdOrderByOrderNumber(s2.getId());
        assertThat(tasks).hasSize(2);
        assertThat(tasks.get(0).getOrderNumber()).isEqualTo(1);
        assertThat(tasks.get(1).getOrderNumber()).isEqualTo(2);
    }
}
