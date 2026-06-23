package com.capcom.aspiro.mapper;

import com.capcom.aspiro.api.dto.response.TemplateResponse;
import com.capcom.aspiro.api.mapper.TemplateMapper;
import com.capcom.aspiro.domain.model.Template;
import com.capcom.aspiro.domain.model.TemplateStage;
import com.capcom.aspiro.domain.model.TemplateTask;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class TemplateMapperTest {

    @Test
    void toDetailsResponse_mapsStagesAndTasks() {
        Template template = Template.builder().id(1L).title("T").description("D").build();
        TemplateStage s = TemplateStage.builder().id(2L).title("S").orderNumber(1).build();
        TemplateTask t = TemplateTask.builder().id(3L).title("Task").description("Desc").orderNumber(1).daysOffset(0).durationDays(2).build();
        s.setTemplateTasks(List.of(t));
        template.setTemplateStages(List.of(s));

        TemplateResponse resp = TemplateMapper.toDetailsResponse(template);

        assertThat(resp).isNotNull();
        assertThat(resp.getStages()).hasSize(1);
        assertThat(resp.getStages().get(0).getTasks()).hasSize(1);
    }
}
