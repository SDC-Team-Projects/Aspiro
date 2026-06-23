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
      <div className="page-header">
        <div>
          <p className="eyebrow">Choose your path</p>
          <h1>Templates</h1>
        </div>
      </div>

      {templates.length === 0 ? (
        <div className="empty-state-card">
          <div className="empty-state-icon">📚</div>

          <h2>No templates found</h2>

          <p>
            There are no active templates available right now. Please check back
            later.
          </p>
        </div>
      ) : (
        <div className="grid">
          {templates.map((template) => (
            <article key={template.id} className="card template-card">
              <div className="template-cover">
                <span className="template-cover-letter">
                  {template.title.charAt(0).toUpperCase()}
                </span>

                {template.coverImageUrl && (
                  <img
                    src={template.coverImageUrl}
                    alt={template.title}
                    onError={(event) => {
                      event.currentTarget.style.display = "none";
                    }}
                  />
                )}
              </div>

              <div className="template-card-content">
                <h2>{template.title}</h2>

                <p>{template.description || "No description"}</p>
              </div>

              <button
                type="button"
                onClick={() => navigate(`/templates/${template.id}`)}
              >
                View details
              </button>
            </article>
          ))}
        </div>
      )}
    </section>
  );
}