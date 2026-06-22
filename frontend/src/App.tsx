import { Navigate, Route, Routes } from "react-router-dom";
import Navbar from "./components/Navbar";
import ProtectedRoute from "./components/ProtectedRoute";
import HomePage from "./pages/HomePage";
import LoginPage from "./pages/LoginPage";
import RegisterPage from "./pages/RegisterPage";
import TemplatesPage from "./pages/TemplatesPage";
import TemplateDetailsPage from "./pages/TemplateDetailsPage";
import MyProgressPage from "./pages/MyProgressPage";
import AdminPage from "./pages/AdminPage";
import GoalDetailsPage from "./pages/GoalDetailsPage";

export default function App() {
  return (
    <>
      <Navbar />

      <main className="page">
        <Routes>
          <Route path="/" element={<HomePage />} />

          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />

          <Route
            path="/templates"
            element={
              <ProtectedRoute>
                <TemplatesPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/templates/:id"
            element={
              <ProtectedRoute>
                <TemplateDetailsPage />
              </ProtectedRoute>
            }
          />

          <Route
            path="/my-progress"
            element={
              <ProtectedRoute>
                <MyProgressPage />
              </ProtectedRoute>
            }
          />
          
          <Route
            path="/goals/:id"
            element={
              <ProtectedRoute>
                <GoalDetailsPage />
              </ProtectedRoute>
            }
          />
          
          <Route
            path="/admin"
            element={
              <ProtectedRoute>
                <AdminPage />
              </ProtectedRoute>
            }
          />

          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </main>
    </>
  );
}