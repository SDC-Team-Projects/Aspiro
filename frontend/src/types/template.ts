export type TemplateTask = {
  id: number;
  title: string;
  description?: string | null;
  orderNumber?: number;
  daysOffset?: number;
  durationDays?: number;
};

export type TemplateStage = {
  id: number;
  title: string;
  orderNumber?: number;
  tasks?: TemplateTask[] | null;
};

export type Template = {
  id: number;
  title: string;
  description?: string | null;
  stages?: TemplateStage[] | null;
};

export type CreateTemplateRequest = {
  title: string;
  description?: string;
};

export type CreateTemplateStageRequest = {
  title: string;
  orderNumber: number;
};

export type CreateTemplateTaskRequest = {
  title: string;
  description?: string;
  orderNumber: number;
  daysOffset: number;
  durationDays: number;
};
export type UpdateTemplateRequest = {
  title: string;
  description?: string;
};

export type UpdateTemplateStageRequest = {
  title: string;
  orderNumber: number;
};

export type UpdateTemplateTaskRequest = {
  title: string;
  description?: string;
  orderNumber: number;
  daysOffset: number;
  durationDays: number;
};