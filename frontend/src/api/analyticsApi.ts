import { apiClient } from "./apiClient";
import type { AnalyticsResponse } from "../types/analytics";

export async function getAnalytics(): Promise<AnalyticsResponse> {
  const response = await apiClient.get<AnalyticsResponse>("/analytics");
  return response.data;
}