import { Link } from "react-router-dom";

export default function HomePage() {
  const isAuthenticated = Boolean(localStorage.getItem("accessToken"));

  return (
    <section className="home-page">
      <div className="hero-card">
        <div className="hero-content">
          <p className="eyebrow">Goal tracking platform</p>

          <h1>Build your path. Track your progress. Stay consistent.</h1>

          <p className="hero-text">
            Aspiro helps users start learning goals from structured templates,
            follow stages, complete tasks, and monitor their progress step by
            step.
          </p>

          <div className="hero-actions">
            {isAuthenticated ? (
              <>
                <Link to="/templates" className="primary-link-button">
                  Explore templates
                </Link>

                <Link to="/my-progress" className="secondary-link-button">
                  My progress
                </Link>
              </>
            ) : (
              <>
                <Link to="/register" className="primary-link-button">
                  Get started
                </Link>

                <Link to="/login" className="secondary-link-button">
                  Sign in
                </Link>
              </>
            )}
          </div>
        </div>

        <div className="hero-preview">
          <div className="preview-window">
            <div className="preview-header">
              <span></span>
              <span></span>
              <span></span>
            </div>

            <div className="preview-body">
              <div className="preview-template-card">
                <p className="preview-label">Template</p>
                <h3>Java Developer Path</h3>
                <p>Learn backend development step by step</p>
              </div>

              <div className="preview-progress-card">
                <div className="preview-progress-top">
                  <span>Progress</span>
                  <strong>68%</strong>
                </div>

                <div className="progress-bar">
                  <div className="progress-fill" style={{ width: "68%" }}></div>
                </div>
              </div>

              <div className="preview-task-card done">
                <span>Done</span>
                <strong>Learn Java variables</strong>
              </div>

              <div className="preview-task-card">
                <span>Next</span>
                <strong>Build REST API</strong>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div className="features-grid">
        <article className="feature-card">
          <h3>Templates</h3>
          <p>Create reusable learning paths with stages and tasks.</p>
        </article>

        <article className="feature-card">
          <h3>Progress</h3>
          <p>Track active goals and complete tasks one by one.</p>
        </article>

        <article className="feature-card">
          <h3>Consistency</h3>
          <p>Follow a clear roadmap and keep moving toward your goal every day.</p>
        </article>
      </div>
    </section>
  );
}