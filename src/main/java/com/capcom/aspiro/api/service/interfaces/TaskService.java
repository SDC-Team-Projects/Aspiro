package com.capcom.aspiro.api.service.interfaces;

import com.capcom.aspiro.api.dto.request.UpdateTaskStatusRequest;
import com.capcom.aspiro.api.dto.response.TaskResponse;

public interface TaskService {

    TaskResponse updateTaskStatus(
            Long taskId,
            UpdateTaskStatusRequest request
    );
}