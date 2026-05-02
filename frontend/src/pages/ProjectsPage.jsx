import { useEffect, useState } from "react";
import client from "../api/client";
import AppLayout from "../components/AppLayout";
import { useAuth } from "../context/AuthContext";
import TaskStatusBadge from "../components/TaskStatusBadge";

function ProjectsPage() {
  const { user } = useAuth();
  const isAdmin = user?.role === "ROLE_ADMIN";
  const [projects, setProjects] = useState([]);
  const [users, setUsers] = useState([]);
  const [projectTasks, setProjectTasks] = useState({});
  const [error, setError] = useState("");
  const [form, setForm] = useState({ name: "", description: "", memberIds: [] });
  const [taskForms, setTaskForms] = useState({});

  const loadProjects = async () => {
    try {
      const { data } = await client.get("/projects");
      setProjects(data);
    } catch (err) {
      setError(err.response?.data?.error || "Failed to load projects");
    }
  };

  const loadUsers = async () => {
    try {
      const { data } = await client.get("/users");
      setUsers(data.filter((u) => u.role === "ROLE_MEMBER"));
    } catch {
      // no-op for demo
    }
  };

  useEffect(() => {
    loadProjects();
    if (isAdmin) loadUsers();
  }, [isAdmin]);

  const createProject = async (e) => {
    e.preventDefault();
    setError("");
    try {
      await client.post("/projects", form);
      setForm({ name: "", description: "", memberIds: [] });
      loadProjects();
    } catch (err) {
      setError(err.response?.data?.error || "Failed to create project");
    }
  };

  const loadProjectTasks = async (projectId) => {
    try {
      const { data } = await client.get(`/tasks/project/${projectId}`);
      setProjectTasks((prev) => ({ ...prev, [projectId]: data }));
    } catch (err) {
      setError(err.response?.data?.error || "Failed to load project tasks");
    }
  };

  const createTask = async (e, projectId) => {
    e.preventDefault();
    const payload = taskForms[projectId];
    if (!payload?.title || !payload?.dueDate || !payload?.assignedUserId) {
      setError("Please fill all task fields");
      return;
    }
    try {
      await client.post(`/tasks/project/${projectId}`, payload);
      setTaskForms((prev) => ({
        ...prev,
        [projectId]: { title: "", description: "", dueDate: "", assignedUserId: "" }
      }));
      loadProjectTasks(projectId);
    } catch (err) {
      setError(err.response?.data?.error || "Failed to create task");
    }
  };

  return (
    <AppLayout>
      <h2>Projects</h2>
      {error && <p className="error">{error}</p>}
      {isAdmin && (
        <form className="panel" onSubmit={createProject}>
          <h3>Create Project</h3>
          <input
            placeholder="Project Name"
            value={form.name}
            onChange={(e) => setForm({ ...form, name: e.target.value })}
            required
          />
          <textarea
            placeholder="Description"
            value={form.description}
            onChange={(e) => setForm({ ...form, description: e.target.value })}
          />
          <label>Assign Team Members</label>
          <select
            multiple
            value={form.memberIds.map(String)}
            onChange={(e) => {
              const values = Array.from(e.target.selectedOptions).map((o) => Number(o.value));
              setForm({ ...form, memberIds: values });
            }}
          >
            {users.map((u) => (
              <option key={u.id} value={u.id}>
                {u.fullName} ({u.email})
              </option>
            ))}
          </select>
          <button type="submit">Create</button>
        </form>
      )}
      <div className="panel">
        <h3>Project List</h3>
        {projects.map((p) => (
          <article key={p.id} className="project-item">
            <h4>{p.name}</h4>
            <p>{p.description}</p>
            <small>Members: {p.members?.map((m) => m.fullName).join(", ") || "None"}</small>
            <div style={{ marginTop: "10px" }}>
              <button type="button" onClick={() => loadProjectTasks(p.id)}>
                View Tasks
              </button>
            </div>

            {isAdmin && (
              <form className="panel" onSubmit={(e) => createTask(e, p.id)}>
                <h4>Create Task</h4>
                <input
                  placeholder="Task title"
                  value={taskForms[p.id]?.title || ""}
                  onChange={(e) =>
                    setTaskForms((prev) => ({
                      ...prev,
                      [p.id]: { ...prev[p.id], title: e.target.value }
                    }))
                  }
                />
                <textarea
                  placeholder="Task description"
                  value={taskForms[p.id]?.description || ""}
                  onChange={(e) =>
                    setTaskForms((prev) => ({
                      ...prev,
                      [p.id]: { ...prev[p.id], description: e.target.value }
                    }))
                  }
                />
                <input
                  type="date"
                  value={taskForms[p.id]?.dueDate || ""}
                  onChange={(e) =>
                    setTaskForms((prev) => ({
                      ...prev,
                      [p.id]: { ...prev[p.id], dueDate: e.target.value }
                    }))
                  }
                />
                <select
                  value={taskForms[p.id]?.assignedUserId || ""}
                  onChange={(e) =>
                    setTaskForms((prev) => ({
                      ...prev,
                      [p.id]: { ...prev[p.id], assignedUserId: Number(e.target.value) }
                    }))
                  }
                >
                  <option value="">Assign user</option>
                  {(p.members || []).map((m) => (
                    <option key={m.id} value={m.id}>
                      {m.fullName}
                    </option>
                  ))}
                </select>
                <button type="submit">Create Task</button>
              </form>
            )}

            {(projectTasks[p.id] || []).map((task) => (
              <div key={task.id} className="task-item">
                <div>
                  <strong>{task.title}</strong>
                  <p>{task.description}</p>
                  <small>Assignee: {task.assignedUserName}</small>
                  <small>Due: {task.dueDate}</small>
                </div>
                <TaskStatusBadge status={task.status} />
              </div>
            ))}
          </article>
        ))}
      </div>
    </AppLayout>
  );
}

export default ProjectsPage;
