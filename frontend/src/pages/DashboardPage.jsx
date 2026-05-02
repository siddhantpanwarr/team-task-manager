import { useEffect, useState } from "react";
import client from "../api/client";
import AppLayout from "../components/AppLayout";

function DashboardPage() {
  const [stats, setStats] = useState(null);
  const [error, setError] = useState("");

  useEffect(() => {
    const load = async () => {
      try {
        const { data } = await client.get("/dashboard/my");
        setStats(data);
      } catch (err) {
        setError(err.response?.data?.error || "Failed to load dashboard");
      }
    };
    load();
  }, []);

  return (
    <AppLayout>
      <h2>Dashboard</h2>
      {error && <p className="error">{error}</p>}
      {stats && (
        <div className="card-grid">
          <div className="card"><h3>Total Tasks</h3><p>{stats.totalTasks}</p></div>
          <div className="card"><h3>Completed</h3><p>{stats.completedTasks}</p></div>
          <div className="card"><h3>Pending</h3><p>{stats.pendingTasks}</p></div>
          <div className="card"><h3>Overdue</h3><p>{stats.overdueTasks}</p></div>
        </div>
      )}
    </AppLayout>
  );
}

export default DashboardPage;
