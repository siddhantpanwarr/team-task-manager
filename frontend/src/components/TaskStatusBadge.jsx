function TaskStatusBadge({ status }) {
  return <span className={`badge ${status}`}>{status}</span>;
}

export default TaskStatusBadge;
