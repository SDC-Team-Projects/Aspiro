import { Navigate } from "react-router-dom";
import type { ReactNode } from "react";

type AdminRouteProps = {
  children: ReactNode;
};

export default function AdminRoute({ children }: AdminRouteProps) {
  const token = localStorage.getItem("accessToken");
  const userRole = localStorage.getItem("userRole");

  if (!token) {
    return <Navigate to="/login" replace />;
  }

  if (userRole !== "ADMIN") {
    return <Navigate to="/templates" replace />;
  }

  return children;
}