import axios from "axios";
import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { createGoal } from "../api/goalApi";
import { getTemplateById } from "../api/templateApi";
import type { Template } from "../types/template";

function getTodayDate() {
  return new Date().toISOString().split("T")[0];
}

export default function TemplateDetailsPage() {
  const { id } = useParams();
  const navigate = useNavigate();

  const [template, setTemplate] = useState<Template | null>(null);
  const [startDate, setStartDate] = useState(getTodayDate());

  const [loading, setLoading] = useState(true);
  const [creating, setCreating] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadTemplate() {
      if (!id) {
        setError("Template id is missing.");
        setLoading(false);
        return;
      }

      try {
        const data = await getTemplateById(Number(id));
        setTemplate(data);
      } catch (error) {
        console.error("Failed to load template:", error);

        if (axios.isAxiosError(error)) {
          console.error("Status:", error.response?.status);
          console.error("Response:", error.response?.data);

          setError(
            `Failed to load template details. Status: ${
              error.response?.status || "network error"
            }`
          );
        } else {
          setError("Failed to load template details.");
        }
      } finally {
        setLoading(false);
      }
    }

    loadTemplate();
  }, [id]);

  async function handleCreateGoal() {
    if (!template) {
      return;
    }

    setError("");
    setCreating(true);

    try {
      await createGoal({
        templateId: template.id,
        title: template.title,
        startDate,
      });

      navigate("/my-progress");
    } catch (error) {
      console.error("Failed to create goal:", error);

      if (axios.isAxiosError(error)) {
        console.error("Status:", error.response?.status);
        console.error("Response:", error.response?.data);

        setError(
          `Failed to create goal. Status: ${
            error.response?.status || "network error"
          }`
        );
      } else {
        setError("Failed to create goal from template.");
      }
    } finally {
      setCreating(false);
    }
  }

  if (loading) {
    return <p>Loading template...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  if (!template) {
    return <p>Template not found.</p>;
  }

  return (
    <section>
      <button
        type="button"
        className="secondary"
        onClick={() => navigate("/templates")}
      >
        Back to templates
      </button>

      <div className="details-card">
        <h1>{template.title}</h1>
        <p>{template.description || "No description"}</p>

        <div className="form">
          <label>
            Start date
            <input
              type="date"
              value={startDate}
              onChange={(event) => setStartDate(event.target.value)}
              required
            />
          </label>

          <button type="button" onClick={handleCreateGoal} disabled={creating}>
            {creating ? "Creating goal..." : "Create goal from this template"}
          </button>
        </div>
      </div>

      <h2>Stages</h2>

      {!template.stages || template.stages.length === 0 ? (
        <p>No stages found for this template.</p>
      ) : (
        <div className="stages-list">
          {template.stages.map((stage) => (
            <article key={stage.id} className="card">
              <h3>{stage.title}</h3>

              {!stage.tasks || stage.tasks.length === 0 ? (
                <p>No tasks in this stage.</p>
              ) : (
                <ul>
                  {stage.tasks.map((task) => (
                    <li key={task.id}>
                      <strong>{task.title}</strong>
                      {task.description && <p>{task.description}</p>}
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