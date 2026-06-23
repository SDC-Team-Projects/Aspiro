import axios from "axios";
import { useState } from "react";
import type { FormEvent } from "react";
import { Link, useNavigate } from "react-router-dom";
import { loginUser } from "../api/authApi";

export default function LoginPage() {
  const navigate = useNavigate();

  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const [error, setError] = useState("");
  const [loading, setLoading] = useState(false);

  async function handleSubmit(event: FormEvent<HTMLFormElement>) {
    event.preventDefault();

    setError("");
    setLoading(true);

    try {
      const response = await loginUser({
        email,
        password,
      });

     localStorage.setItem("accessToken", response.accessToken);
     localStorage.setItem("userRole", response.role);

     if (response.refreshToken) {
        localStorage.setItem("refreshToken", response.refreshToken);
      }

      navigate("/templates");
    } catch (error) {
      console.error("Login failed:", error);

      if (axios.isAxiosError(error)) {
        if (error.response?.status === 401 || error.response?.status === 400) {
          setError("Invalid email or password.");
        } else {
          setError("Login failed. Please try again later.");
        }
      } else {
        setError("Login failed.");
      }
    } finally {
      setLoading(false);
    }
  }

  return (
    <section className="auth-page">
      <div className="auth-panel">
        <div className="auth-info">
          <p className="eyebrow">Welcome back</p>
          <h1>Continue building your goals.</h1>
          <p>
            Sign in to access templates, track your active goals, and manage
            your learning progress.
          </p>
        </div>

        <div className="card auth-form-card">
          <h2>Login</h2>

          {error && <p className="error">{error}</p>}

          <form className="form" onSubmit={handleSubmit}>
            <label>
              Email
              <input
                type="email"
                value={email}
                onChange={(event) => setEmail(event.target.value)}
                placeholder="you@example.com"
                required
              />
            </label>

            <label>
              Password
              <input
                type="password"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                placeholder="Your password"
                required
              />
            </label>

            <button type="submit" disabled={loading}>
              {loading ? "Signing in..." : "Login"}
            </button>
          </form>

          <p className="auth-switch">
            No account yet? <Link to="/register">Create one</Link>
          </p>
        </div>
      </div>
    </section>
  );
}