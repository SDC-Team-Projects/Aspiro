import { apiClient } from "./apiClient";
import type { GoalTask, UpdateTaskStatusRequest } from "../types/goal";

export async function updateTaskStatus(
  taskId: number,
  request: UpdateTaskStatusRequest
): Promise<GoalTask> {
  const response = await apiClient.patch<GoalTask>(`/tasks/${taskId}`, request);

  return response.data;
}