import { Link } from "react-router-dom";

export default function HomePage() {
  return (
    <section className="hero">
      <h1>Aspiro</h1>

      <p>
        Build your personal goals from structured templates and track your
        progress step by step.
      </p>

      <div className="hero-actions">
        <Link to="/login" className="button">
          Login
        </Link>

        <Link to="/templates" className="button secondary">
          View Templates
        </Link>
      </div>
    </section>
  );
}