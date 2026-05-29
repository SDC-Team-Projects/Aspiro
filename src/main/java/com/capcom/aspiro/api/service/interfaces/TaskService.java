package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.request.UpdateTaskStatusRequest;
import com.capcom.aspiro.api.dto.response.GoalTaskResponse;

public interface TaskService {

    GoalTaskResponse updateTaskStatus(
            Long taskId,
            UpdateTaskStatusRequest request
    );
}