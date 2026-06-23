package com.capcom.aspiro.domain.repository;

import com.capcom.aspiro.domain.model.Template;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TemplateRepository extends JpaRepository<Template, Long> {

    List<Template> findByArchivedFalse();
}