import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getGoalById } from "../api/goalApi";
import { updateTaskStatus } from "../api/taskApi";
import type { GoalDetailed } from "../types/goal";

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

  if (loading) {
    return <p>Loading goal...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  if (!goal) {
    return <p>Goal not found.</p>;
  }

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
        <h1>{goal.title}</h1>

        <p>Status: {goal.status}</p>

        {goal.startDate && <p>Start date: {goal.startDate}</p>}

        {goal.endDate && <p>End date: {goal.endDate}</p>}

        <p>Progress: {goal.progress ?? 0}%</p>

        <div className="progress-bar">
          <div
            className="progress-bar-fill"
            style={{ width: `${goal.progress ?? 0}%` }}
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
              <h3>{stage.title}</h3>

              {!stage.tasks || stage.tasks.length === 0 ? (
                <p>No tasks in this stage.</p>
              ) : (
                <ul className="task-list">
                  {stage.tasks.map((task) => (
                    <li key={task.id} className="task-item">
                      <div>
                        <strong>{task.title}</strong>
                        <span className={`status-badge status-${task.status}`}>
                          {task.status}
                        </span>

                        <div className="task-dates">
                          {task.startDate && <span>Start: {task.startDate}</span>}
                          {task.endDate && <span>End: {task.endDate}</span>}
                        </div>
                      </div>

                      {task.status !== "DONE" && (
                        <button
                          type="button"
                          onClick={() => handleMarkTaskDone(task.id)}
                          disabled={updatingTaskId === task.id}
                        >
                          {updatingTaskId === task.id
                            ? "Updating..."
                            : "Mark as done"}
                        </button>
                      )}
                    </li>
                  ))}
                </ul>
              )}
            </article>
          ))}
        </div>
      )}
    </section>
  );
}