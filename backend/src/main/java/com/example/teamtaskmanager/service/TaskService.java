package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.dto.task.TaskRequest;
import com.example.teamtaskmanager.dto.task.TaskResponse;
import com.example.teamtaskmanager.entity.TaskStatus;

import java.util.List;

public interface TaskService {
    TaskResponse createTask(Long projectId, TaskRequest request, String adminEmail);
    List<TaskResponse> getTasksByProject(Long projectId, String userEmail);
    List<TaskResponse> getMyTasks(String userEmail);
    TaskResponse updateTaskStatus(Long taskId, TaskStatus status, String userEmail);
}
