package com.capcom.aspiro.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.enums.TaskStatus;

public interface GoalTaskRepository extends JpaRepository<GoalTask, Long> {

    List<GoalTask> findByGoalStageId(Long goalStageId);

    List<GoalTask> findByStatus(TaskStatus status);
}