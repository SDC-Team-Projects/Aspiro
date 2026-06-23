import { apiClient } from "./apiClient";
import type {
  CreateTemplateRequest,
  CreateTemplateStageRequest,
  CreateTemplateTaskRequest,
  Template,
  UpdateTemplateRequest,
  UpdateTemplateStageRequest,
  UpdateTemplateTaskRequest,
} from "../types/template";

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

export async function getTemplates(): Promise<Template[]> {
  const response = await apiClient.get<ApiResponse<Template[]> | Template[]>(
    "/templates"
  );

  return unwrapData<Template[]>(response.data);
}

export async function getTemplateById(id: number): Promise<Template> {
  const response = await apiClient.get<ApiResponse<Template> | Template>(
    `/templates/${id}`
  );

  return unwrapData<Template>(response.data);
}

export async function createTemplate(
  request: CreateTemplateRequest
): Promise<Template> {
  const response = await apiClient.post<Template>("/templates", request);

  return response.data;
}

export async function createTemplateStage(
  templateId: number,
  request: CreateTemplateStageRequest
): Promise<void> {
  await apiClient.post(`/templates/${templateId}/stages`, request);
}

export async function createTemplateTask(
  stageId: number,
  request: CreateTemplateTaskRequest
): Promise<void> {
  await apiClient.post(`/template-stages/${stageId}/tasks`, request);
}

export async function deleteTemplate(id: number): Promise<void> {
  await apiClient.delete(`/templates/${id}`);
}

export async function deleteTemplateStage(id: number): Promise<void> {
  await apiClient.delete(`/template-stages/${id}`);
}

export async function deleteTemplateTask(id: number): Promise<void> {
  await apiClient.delete(`/template-tasks/${id}`);
}

export async function updateTemplate(
  id: number,
  request: UpdateTemplateRequest
): Promise<Template> {
  const response = await apiClient.patch<Template>(`/templates/${id}`, request);

  return response.data;
}

export async function updateTemplateStage(
  id: number,
  request: UpdateTemplateStageRequest
): Promise<void> {
  await apiClient.patch(`/template-stages/${id}`, request);
}

export async function updateTemplateTask(
  id: number,
  request: UpdateTemplateTaskRequest
): Promise<void> {
  await apiClient.patch(`/template-tasks/${id}`, request);
}

export async function getAdminTemplates(): Promise<Template[]> {
  const response = await apiClient.get<ApiResponse<Template[]> | Template[]>(
    "/templates/admin"
  );

  return unwrapData<Template[]>(response.data);
}

export async function archiveTemplate(id: number): Promise<Template> {
  const response = await apiClient.patch<Template>(`/templates/${id}/archive`);

  return response.data;
}

export async function restoreTemplate(id: number): Promise<Template> {
  const response = await apiClient.patch<Template>(`/templates/${id}/restore`);

  return response.data;
}