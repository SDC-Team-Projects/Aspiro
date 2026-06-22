export type TaskStatus = "TODO" | "IN_PROGRESS" | "DONE";

export type GoalTask = {
  id: number;
  title: string;
  status: TaskStatus | string;
  startDate?: string | null;
  endDate?: string | null;
};

export type GoalStage = {
  id: number;
  title: string;
  orderNumber?: number | null;
  tasks?: GoalTask[] | null;
};

export type Goal = {
  id: number;
  title: string;
  status: string;
  progress?: number | null;
  startDate?: string | null;
  endDate?: string | null;
};

export type GoalDetailed = {
  id: number;
  title: string;
  status: string;
  startDate?: string | null;
  endDate?: string | null;
  progress?: number | null;
  stages?: GoalStage[] | null;
};

export type CreateGoalRequest = {
  templateId: number;
  title: string;
  startDate: string;
};

export type UpdateTaskStatusRequest = {
  status: TaskStatus;
};