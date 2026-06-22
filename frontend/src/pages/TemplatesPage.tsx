import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getTemplates } from "../api/templateApi";
import type { Template } from "../types/template";

export default function TemplatesPage() {
  const navigate = useNavigate();

  const [templates, setTemplates] = useState<Template[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    async function loadTemplates() {
      try {
        const data = await getTemplates();
        setTemplates(data);
      } catch (error) {
        console.error("Failed to load templates:", error);
        setError("Failed to load templates from backend.");
      } finally {
        setLoading(false);
      }
    }

    loadTemplates();
  }, []);

  if (loading) {
    return <p>Loading templates...</p>;
  }

  if (error) {
    return <p className="error">{error}</p>;
  }

  return (
    <section>
      <h1>Templates</h1>

      {templates.length === 0 && <p>No templates found.</p>}

      <div className="grid">
        {templates.map((template) => (
          <article key={template.id} className="card">
            <h2>{template.title}</h2>
            <p>{template.description || "No description"}</p>

            <button
              type="button"
              onClick={() => navigate(`/templates/${template.id}`)}
            >
              View details
            </button>
          </article>
        ))}
      </div>
    </section>
  );
}