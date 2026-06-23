package com.capcom.aspiro.service;

import com.capcom.aspiro.api.dto.request.CreateTemplateRequest;
import com.capcom.aspiro.api.service.impl.TemplateServiceImpl;
import com.capcom.aspiro.domain.model.Template;
import com.capcom.aspiro.domain.repository.TemplateRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TemplateServiceImplTest {

    @Mock
    private TemplateRepository templateRepository;

    @InjectMocks
    private TemplateServiceImpl templateService;

    @Test
    void getTemplateById_notFound_throws() {
        when(templateRepository.findById(5L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> templateService.getTemplateById(5L));
    }

    @Test
    void createTemplate_savesAndReturnsResponse() {
        CreateTemplateRequest req = new CreateTemplateRequest();
        req.setTitle("T");
        req.setDescription("D");

        Template t = Template.builder().id(10L).title("T").description("D").build();
        when(templateRepository.save(any(Template.class))).thenReturn(t);

        var resp = templateService.createTemplate(req);

        assertThat(resp.getId()).isEqualTo(10L);
        verify(templateRepository, times(1)).save(any(Template.class));
    }
}
