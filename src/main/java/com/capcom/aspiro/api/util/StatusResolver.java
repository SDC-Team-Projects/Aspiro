package com.capcom.aspiro.api.util;

import java.time.LocalDate;

import com.capcom.aspiro.domain.model.enums.GoalStatus;
import com.capcom.aspiro.domain.model.enums.TaskStatus;

public final class StatusResolver {

    private StatusResolver() {
    }

    public static TaskStatus resolveTaskStatus(TaskStatus currentStatus, LocalDate endDate) {
        if (currentStatus == TaskStatus.DONE) {
            return TaskStatus.DONE;
        }

        if (endDate != null && endDate.isBefore(LocalDate.now())) {
            return TaskStatus.OVERDUE;
        }

        return currentStatus;
    }

    public static GoalStatus resolveGoalStatus(GoalStatus currentStatus, LocalDate endDate) {
        if (currentStatus == GoalStatus.COMPLETED) {
            return GoalStatus.COMPLETED;
        }

        if (endDate != null && endDate.isBefore(LocalDate.now())) {
            return GoalStatus.OVERDUE;
        }

        return currentStatus;
    }
}