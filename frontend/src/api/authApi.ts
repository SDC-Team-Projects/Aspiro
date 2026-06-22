import { apiClient } from "./apiClient";
import type { AuthResponse, LoginRequest, RegisterRequest } from "../types/auth";

export async function loginUser(request: LoginRequest): Promise<AuthResponse> {
  const response = await apiClient.post<AuthResponse>("/auth/login", request);
  return response.data;
}

export async function registerUser(request: RegisterRequest): Promise<void> {
  await apiClient.post("/auth/register", request);
}