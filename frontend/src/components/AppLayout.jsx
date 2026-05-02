import { Link, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";

function AppLayout({ children }) {
  const { user, logout } = useAuth();
  const navigate = useNavigate();

  return (
    <div className="app-shell">
      <header className="app-header">
        <h1>Team Task Manager</h1>
        <nav>
          <Link to="/dashboard">Dashboard</Link>
          <Link to="/projects">Projects</Link>
          <Link to="/tasks">My Tasks</Link>
        </nav>
        <div className="user-box">
          <span>{user?.fullName}</span>
          <button
            onClick={() => {
              logout();
              navigate("/login");
            }}
          >
            Logout
          </button>
        </div>
      </header>
      <main className="app-main">{children}</main>
    </div>
  );
}

export default AppLayout;
