import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getUserGoals } from "../api/goalApi";
import type { Goal } from "../types/goal";

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

function getStatusClassName(status: string): string {
  return status.toLowerCase().replaceAll("_", "-");
}

export default function MyProgressPage() {
  const navigate = useNavigate();

  const [goals, setGoals] = useState<Goal[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadGoals() {
      try {
        const data = await getUserGoals();

        console.log("Goals response:", data);

        setGoals(data);
      } catch (error) {
        console.error("Failed to load goals:", error);

        if (axios.isAxiosError(error)) {
          console.error("Status:", error.response?.status);
          console.error("Response:", error.response?.data);

          setError(
            `Failed to load goals. Status: ${
              error.response?.status || "network error"
            }`
          );
        } else {
          setError("Failed to load goals.");
        }
      } finally {
        setLoading(false);
      }
    }

    loadGoals();
  }, []);

  if (loading) {
    return <p>Loading goals...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  return (
    <section>
      <div className="page-header">
        <div>
          <p className="eyebrow">Your learning paths</p>
          <h1>My Progress</h1>
        </div>

        {goals.length > 0 && (
          <button type="button" onClick={() => navigate("/templates")}>
            Start new goal
          </button>
        )}
      </div>

      {goals.length === 0 ? (
        <div className="empty-state-card">
          <div className="empty-state-icon">🎯</div>

          <h2>No goals yet</h2>

          <p>
            Choose a template and start your first learning path. Your active
            goals, deadlines and progress will appear here.
          </p>

          <button
  type="button"
  className="empty-state-button"
  onClick={() => navigate("/templates")}
>
  Browse templates
</button>
        </div>
      ) : (
        <div className="grid">
          {goals.map((goal) => {
            const progress = goal.progress ?? 0;

            return (
              <article key={goal.id} className="card goal-summary-card">
                <div className="goal-card-header">
                  <h2>{goal.title}</h2>

                  <span
                    className={`goal-status-badge goal-status-${getStatusClassName(
                      goal.status
                    )}`}
                  >
                    {goal.status}
                  </span>
                </div>

                <div className="goal-card-meta">
                  <span>Started: {formatDate(goal.startDate)}</span>

                  {goal.endDate && (
                    <span>Deadline: {formatDate(goal.endDate)}</span>
                  )}
                </div>

                <p className="goal-progress-text">Progress: {progress}%</p>

                <div className="progress-bar">
                  <div
                    className="progress-bar-fill"
                    style={{ width: `${progress}%` }}
                  />
                </div>

                <button
                  type="button"
                  onClick={() => navigate(`/goals/${goal.id}`)}
                >
                  Open goal
                </button>
              </article>
            );
          })}
        </div>
      )}
    </section>
  );
}