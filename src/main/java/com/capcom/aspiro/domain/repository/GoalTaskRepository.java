package com.capcom.aspiro.domain.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.capcom.aspiro.domain.model.GoalTask;
import com.capcom.aspiro.domain.model.enums.TaskStatus;
import java.util.Optional;

public interface GoalTaskRepository extends JpaRepository<GoalTask, Long> {

    List<GoalTask> findByStatus(TaskStatus status);

    List<GoalTask> findByGoalStageId(Long goalStageId);

    @Query("""
            select task
            from GoalTask task
            join fetch task.goalStage stage
            join fetch stage.goal goal
            join fetch goal.user user
            where task.id = :taskId
            """)
    Optional<GoalTask> findByIdWithGoalAndUser(@Param("taskId") Long taskId);
}