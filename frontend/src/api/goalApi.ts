import { apiClient } from "./apiClient";
import type { CreateGoalRequest, Goal, GoalDetailed } from "../types/goal";

type ApiResponse<T> = {
  data: T;
};

function unwrapData<T>(responseData: T | ApiResponse<T>): T {
  if (
    responseData &&
    typeof responseData === "object" &&
    "data" in responseData
  ) {
    return (responseData as ApiResponse<T>).data;
  }

  return responseData as T;
}

export async function createGoal(request: CreateGoalRequest): Promise<Goal> {
  const response = await apiClient.post<ApiResponse<Goal> | Goal>(
    "/goals",
    request
  );

  return unwrapData<Goal>(response.data);
}

export async function getUserGoals(): Promise<Goal[]> {
  const response = await apiClient.get<ApiResponse<Goal[]> | Goal[]>("/goals");

  return unwrapData<Goal[]>(response.data);
}

export async function getGoalById(id: number): Promise<GoalDetailed> {
  const response = await apiClient.get<ApiResponse<GoalDetailed> | GoalDetailed>(
    `/goals/${id}`
  );

  return unwrapData<GoalDetailed>(response.data);
}