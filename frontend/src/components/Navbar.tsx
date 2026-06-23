import { Link, NavLink, useLocation, useNavigate } from "react-router-dom";

export default function Navbar() {
  const navigate = useNavigate();
  const location = useLocation();

  const isAuthenticated = Boolean(localStorage.getItem("accessToken"));
  const userRole = localStorage.getItem("userRole");
  const isAdmin = userRole === "ADMIN";

  function handleLogout() {
    localStorage.removeItem("accessToken");
    localStorage.removeItem("refreshToken");
    localStorage.removeItem("userRole");

    navigate("/login");
  }

  return (
    <header className="navbar">
      <Link to="/" className="logo">
        Aspiro
      </Link>

      <nav className="nav-links">
        {isAuthenticated ? (
          <>
            <NavLink
              to="/templates"
              className={({ isActive }) =>
                isActive ? "nav-link active" : "nav-link"
              }
            >
              Templates
            </NavLink>

            <NavLink
              to="/my-progress"
              className={({ isActive }) =>
                isActive ? "nav-link active" : "nav-link"
              }
            >
              My Progress
            </NavLink>

            {isAdmin && (
              <NavLink
                to="/admin"
                className={({ isActive }) =>
                  isActive ? "nav-link active" : "nav-link"
                }
              >
                Admin
              </NavLink>
            )}

            <button
              type="button"
              className="logout-button"
              onClick={handleLogout}
            >
              Logout
            </button>
          </>
        ) : (
          <>
            {location.pathname !== "/login" && (
              <NavLink
                to="/login"
                className={({ isActive }) =>
                  isActive ? "nav-link active" : "nav-link"
                }
              >
                Login
              </NavLink>
            )}

            {location.pathname !== "/register" && (
              <NavLink
                to="/register"
                className={({ isActive }) =>
                  isActive ? "nav-link active" : "nav-link"
                }
              >
                Register
              </NavLink>
            )}
          </>
        )}
      </nav>
    </header>
  );
}