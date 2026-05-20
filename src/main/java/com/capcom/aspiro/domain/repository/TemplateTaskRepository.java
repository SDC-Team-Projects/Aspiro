package com.capcom.aspiro.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capcom.aspiro.domain.model.TemplateTask;

public interface TemplateTaskRepository extends JpaRepository<TemplateTask, Long> {
}