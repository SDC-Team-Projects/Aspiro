import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getUserGoals } from "../api/goalApi";
import type { Goal } from "../types/goal";

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
      <h1>My Progress</h1>

      {goals.length === 0 && (
        <div className="card">
          <h2>No goals yet</h2>
          <p>Create your first goal from a template.</p>

          <button type="button" onClick={() => navigate("/templates")}>
            Go to templates
          </button>
        </div>
      )}

      <div className="grid">
        {goals.map((goal) => (
          <article key={goal.id} className="card">
            <h2>{goal.title}</h2>

            <p>Status: {goal.status}</p>

            {goal.startDate && <p>Start date: {goal.startDate}</p>}

            <p>Progress: {goal.progress ?? 0}%</p>

            <button type="button" onClick={() => navigate(`/goals/${goal.id}`)}>
              Open goal
            </button>
          </article>
        ))}
      </div>
    </section>
  );
}