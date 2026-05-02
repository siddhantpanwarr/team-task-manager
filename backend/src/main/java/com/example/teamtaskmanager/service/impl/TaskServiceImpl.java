package com.example.teamtaskmanager.service.impl;

import com.example.teamtaskmanager.dto.task.TaskRequest;
import com.example.teamtaskmanager.dto.task.TaskResponse;
import com.example.teamtaskmanager.entity.*;
import com.example.teamtaskmanager.exception.BadRequestException;
import com.example.teamtaskmanager.exception.ResourceNotFoundException;
import com.example.teamtaskmanager.repository.ProjectRepository;
import com.example.teamtaskmanager.repository.TaskRepository;
import com.example.teamtaskmanager.repository.UserRepository;
import com.example.teamtaskmanager.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    public TaskResponse createTask(Long projectId, TaskRequest request, String adminEmail) {
        User admin = getUser(adminEmail);
        assertAdmin(admin);
        Project project = getProject(projectId);
        if (!project.getCreatedBy().getId().equals(admin.getId())) {
            throw new BadRequestException("Only project creator can assign tasks");
        }

        User assignee = userRepository.findById(request.getAssignedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("Assigned user not found"));
        if (!project.getTeamMembers().contains(assignee)) {
            throw new BadRequestException("Assigned user must be part of project members");
        }

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setStatus(TaskStatus.TODO);
        task.setProject(project);
        task.setAssignedUser(assignee);
        return toResponse(taskRepository.save(task));
    }

    @Override
    public List<TaskResponse> getTasksByProject(Long projectId, String userEmail) {
        User current = getUser(userEmail);
        Project project = getProject(projectId);
        if (current.getRole() == Role.ROLE_MEMBER && !project.getTeamMembers().contains(current)) {
            throw new BadRequestException("You are not part of this project");
        }
        return taskRepository.findByProjectId(projectId).stream().map(this::toResponse).toList();
    }

    @Override
    public List<TaskResponse> getMyTasks(String userEmail) {
        User current = getUser(userEmail);
        return taskRepository.findByAssignedUserId(current.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public TaskResponse updateTaskStatus(Long taskId, TaskStatus status, String userEmail) {
        User current = getUser(userEmail);
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task not found with id: " + taskId));

        boolean owner = task.getAssignedUser().getId().equals(current.getId());
        boolean creatorAdmin = current.getRole() == Role.ROLE_ADMIN
                && task.getProject().getCreatedBy().getId().equals(current.getId());
        if (!owner && !creatorAdmin) {
            throw new BadRequestException("You cannot update this task");
        }
        task.setStatus(status);
        return toResponse(taskRepository.save(task));
    }

    private User getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    private Project getProject(Long id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Project not found with id: " + id));
    }

    private void assertAdmin(User user) {
        if (user.getRole() != Role.ROLE_ADMIN) {
            throw new BadRequestException("Only admins can perform this operation");
        }
    }

    private TaskResponse toResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .dueDate(task.getDueDate())
                .projectId(task.getProject().getId())
                .projectName(task.getProject().getName())
                .assignedUserId(task.getAssignedUser().getId())
                .assignedUserName(task.getAssignedUser().getFullName())
                .createdAt(task.getCreatedAt())
                .build();
    }
}
