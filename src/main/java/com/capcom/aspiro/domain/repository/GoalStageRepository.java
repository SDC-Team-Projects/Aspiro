package com.capcom.aspiro.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capcom.aspiro.domain.model.GoalStage;

public interface GoalStageRepository extends JpaRepository<GoalStage, Long> {

    List<GoalStage> findByGoalId(Long goalId);
}