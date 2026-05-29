package com.capcom.aspiro.config;

import com.capcom.aspiro.domain.model.Template;
import com.capcom.aspiro.domain.repository.TemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TemplateInitializer implements CommandLineRunner {

    private final TemplateRepository templateRepository;

    @Override
    public void run(String... args) {
        if (templateRepository.count() > 0) {
            return;
        }

        Template template = Template.builder()
                .title("Default template")
                .description("Template for testing goals")
                .build();

        templateRepository.save(template);
    }
}