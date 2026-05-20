package com.capcom.aspiro.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capcom.aspiro.domain.model.Template;

public interface TemplateRepository extends JpaRepository<Template, Long> {
}