import axios from "axios";
import { useEffect, useMemo, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getGoalById } from "../api/goalApi";
import { updateTaskStatus } from "../api/taskApi";
import type { GoalDetailed, GoalTask } from "../types/goal";

function parseLocalDate(dateString?: string | null): Date | null {
  if (!dateString) {
    return null;
  }

  const [year, month, day] = dateString.split("-").map(Number);

  if (!year || !month || !day) {
    return null;
  }

  return new Date(year, month - 1, day);
}

function formatDate(dateString?: string | null): string {
  const date = parseLocalDate(dateString);

  if (!date) {
    return "No date";
  }

  return date.toLocaleDateString("en-GB", {
    day: "2-digit",
    month: "short",
    year: "numeric",
  });
}

function isSameDay(first: Date, second: Date): boolean {
  return (
    first.getFullYear() === second.getFullYear() &&
    first.getMonth() === second.getMonth() &&
    first.getDate() === second.getDate()
  );
}

function isTaskOverdue(task: GoalTask): boolean {
  if (task.status === "DONE") {
    return false;
  }

  if (task.status === "OVERDUE") {
    return true;
  }

  const deadline = parseLocalDate(task.endDate);

  if (!deadline) {
    return false;
  }

  const today = new Date();

  today.setHours(0, 0, 0, 0);
  deadline.setHours(0, 0, 0, 0);

  return deadline < today;
}

function getTaskDeadlineStatus(taskStatus: string, endDate?: string | null) {
  if (taskStatus === "DONE") {
    return {
      label: "Completed",
      className: "deadline-badge completed",
    };
  }

  if (taskStatus === "OVERDUE") {
    return {
      label: "Overdue",
      className: "deadline-badge overdue",
    };
  }

  const deadline = parseLocalDate(endDate);

  if (!deadline) {
    return {
      label: "No deadline",
      className: "deadline-badge neutral",
    };
  }

  const today = new Date();

  today.setHours(0, 0, 0, 0);
  deadline.setHours(0, 0, 0, 0);

  if (deadline < today) {
    return {
      label: "Overdue",
      className: "deadline-badge overdue",
    };
  }

  if (isSameDay(deadline, today)) {
    return {
      label: "Due today",
      className: "deadline-badge due-today",
    };
  }

  return {
    label: "Upcoming",
    className: "deadline-badge upcoming",
  };
}

function getStatusClassName(status: string): string {
  return status.toLowerCase().replaceAll("_", "-");
}

export default function GoalDetailsPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [goal, setGoal] = useState<GoalDetailed | null>(null);
  const [loading, setLoading] = useState(true);
  const [updatingTaskId, setUpdatingTaskId] = useState<number | null>(null);
  const [error, setError] = useState("");

  async function loadGoal() {
    if (!id) {
      setError("Goal id is missing.");
      setLoading(false);
      return;
    }

    try {
      const data = await getGoalById(Number(id));

      console.log("Goal details response:", data);

      setGoal(data);
    } catch (error) {
      console.error("Failed to load goal:", error);

      if (axios.isAxiosError(error)) {
        console.error("Status:", error.response?.status);
        console.error("Response:", error.response?.data);

        setError(
          `Failed to load goal. Status: ${
            error.response?.status || "network error"
          }`
        );
      } else {
        setError("Failed to load goal.");
      }
    } finally {
      setLoading(false);
    }
  }

  useEffect(() => {
    loadGoal();
  }, [id]);

  async function handleMarkTaskDone(taskId: number) {
    setError("");
    setUpdatingTaskId(taskId);

    try {
      await updateTaskStatus(taskId, {
        status: "DONE",
      });

      await loadGoal();
    } catch (error) {
      console.error("Failed to update task status:", error);

      if (axios.isAxiosError(error)) {
        console.error("Status:", error.response?.status);
        console.error("Response:", error.response?.data);

        setError(
          `Failed to update task status. Status: ${
            error.response?.status || "network error"
          }`
        );
      } else {
        setError("Failed to update task status.");
      }
    } finally {
      setUpdatingTaskId(null);
    }
  }

  const goalAnalytics = useMemo(() => {
    const allTasks = goal?.stages?.flatMap((stage) => stage.tasks ?? []) ?? [];

    const totalTasks = allTasks.length;

    const doneTasks = allTasks.filter((task) => task.status === "DONE").length;

    const overdueTasks = allTasks.filter((task) => isTaskOverdue(task)).length;

    const remainingTasks = totalTasks - doneTasks;

    const computedProgress =
      totalTasks > 0 ? Math.round((doneTasks * 100) / totalTasks) : 0;

    return {
      totalTasks,
      doneTasks,
      overdueTasks,
      remainingTasks,
      computedProgress,
    };
  }, [goal]);

  if (loading) {
    return <p>Loading goal...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  if (!goal) {
    return <p>Goal not found.</p>;
  }

  const progress = goalAnalytics.computedProgress;
  const isGoalCompleted = progress >= 100 || goal.status === "COMPLETED";

  return (
    <section>
      <button
        type="button"
        className="secondary"
        onClick={() => navigate("/my-progress")}
      >
        Back to my progress
      </button>

      <div className="details-card">
        <div className="goal-details-header">
          <div>
            <h1>{goal.title}</h1>

            <div className="goal-date-summary">
              <span>Started: {formatDate(goal.startDate)}</span>
              <span>Goal deadline: {formatDate(goal.endDate)}</span>
            </div>
          </div>

          <span
            className={`goal-status-badge goal-status-${getStatusClassName(
              goal.status
            )}`}
          >
            {goal.status}
          </span>
        </div>

        {isGoalCompleted && (
          <p className="goal-completed-message">
            Goal completed! All planned work is finished.
          </p>
        )}

        <section className="goal-analytics-panel">
          <div className="goal-analytics-card">
            <span className="analytics-label">Goal progress</span>
            <strong>{progress}%</strong>
          </div>

          <div className="goal-analytics-card">
            <span className="analytics-label">Tasks done</span>
            <strong>
              {goalAnalytics.doneTasks} / {goalAnalytics.totalTasks}
            </strong>
          </div>

          <div className="goal-analytics-card">
            <span className="analytics-label">Overdue tasks</span>
            <strong>{goalAnalytics.overdueTasks}</strong>
          </div>

          <div className="goal-analytics-card">
            <span className="analytics-label">Remaining tasks</span>
            <strong>{goalAnalytics.remainingTasks}</strong>
          </div>
        </section>

        <p>Progress: {progress}%</p>

        <div className="progress-bar">
          <div
            className="progress-bar-fill"
            style={{ width: `${progress}%` }}
          />
        </div>
      </div>

      <h2>Stages</h2>

      {!goal.stages || goal.stages.length === 0 ? (
        <p>No stages found for this goal.</p>
      ) : (
        <div className="stages-list">
          {goal.stages.map((stage) => (
            <article key={stage.id} className="card">
              <h3>
                {stage.orderNumber ? `${stage.orderNumber}. ` : ""}
                {stage.title}
              </h3>

              {!stage.tasks || stage.tasks.length === 0 ? (
                <p>No tasks in this stage.</p>
              ) : (
                <div className="goal-task-list">
                  {stage.tasks.map((task) => {
                    const deadlineInfo = getTaskDeadlineStatus(
                      task.status,
                      task.endDate
                    );

                    return (
                      <div key={task.id} className="goal-task-card">
                        <div className="goal-task-main">
                          <div className="task-title-row">
                            <strong>{task.title}</strong>

                            <span
                              className={`task-status-badge task-status-${getStatusClassName(
                                task.status
                              )}`}
                            >
                              {task.status}
                            </span>
                          </div>

                          <div className="task-deadline-info">
                            <span>Start: {formatDate(task.startDate)}</span>
                            <span>Deadline: {formatDate(task.endDate)}</span>

                            <span className={deadlineInfo.className}>
                              {deadlineInfo.label}
                            </span>
                          </div>
                        </div>

                        {task.status !== "DONE" && (
                          <button
                            type="button"
                            className="task-done-button"
                            onClick={() => handleMarkTaskDone(task.id)}
                            disabled={updatingTaskId === task.id}
                          >
                            {updatingTaskId === task.id
                              ? "Updating..."
                              : "Mark as done"}
                          </button>
                        )}
                      </div>
                    );
                  })}
                </div>
              )}
            </article>
          ))}
        </div>
      )}
    </section>
  );
}