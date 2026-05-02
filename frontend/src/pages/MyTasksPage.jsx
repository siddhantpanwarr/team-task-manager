import { useEffect, useState } from "react";
import client from "../api/client";
import AppLayout from "../components/AppLayout";
import TaskStatusBadge from "../components/TaskStatusBadge";

function MyTasksPage() {
  const [tasks, setTasks] = useState([]);
  const [error, setError] = useState("");

  const loadTasks = async () => {
    try {
      const { data } = await client.get("/tasks/my");
      setTasks(data);
    } catch (err) {
      setError(err.response?.data?.error || "Failed to load tasks");
    }
  };

  useEffect(() => {
    loadTasks();
  }, []);

  const updateStatus = async (taskId, status) => {
    try {
      await client.patch(`/tasks/${taskId}/status`, { status });
      loadTasks();
    } catch (err) {
      setError(err.response?.data?.error || "Status update failed");
    }
  };

  return (
    <AppLayout>
      <h2>My Tasks</h2>
      {error && <p className="error">{error}</p>}
      <div className="panel">
        {tasks.map((task) => (
          <article className="task-item" key={task.id}>
            <div>
              <h4>{task.title}</h4>
              <p>{task.description}</p>
              <small>Project: {task.projectName}</small>
              <small>Due: {task.dueDate}</small>
            </div>
            <div className="status-group">
              <TaskStatusBadge status={task.status} />
              <select
                value={task.status}
                onChange={(e) => updateStatus(task.id, e.target.value)}
              >
                <option value="TODO">TODO</option>
                <option value="IN_PROGRESS">IN_PROGRESS</option>
                <option value="DONE">DONE</option>
              </select>
            </div>
          </article>
        ))}
      </div>
    </AppLayout>
  );
}

export default MyTasksPage;
