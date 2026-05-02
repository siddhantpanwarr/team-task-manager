package com.example.teamtaskmanager.controller;

import com.example.teamtaskmanager.dto.task.TaskRequest;
import com.example.teamtaskmanager.dto.task.TaskResponse;
import com.example.teamtaskmanager.dto.task.TaskStatusUpdateRequest;
import com.example.teamtaskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tasks")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:5173}")
public class TaskController {

    private final TaskService taskService;

    @PostMapping("/project/{projectId}")
    public ResponseEntity<TaskResponse> createTask(
            @PathVariable Long projectId,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(taskService.createTask(projectId, request, email));
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<TaskResponse>> getTasksByProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(taskService.getTasksByProject(projectId, email));
    }

    @GetMapping("/my")
    public ResponseEntity<List<TaskResponse>> getMyTasks(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(taskService.getMyTasks(email));
    }

    @PatchMapping("/{taskId}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long taskId,
            @Valid @RequestBody TaskStatusUpdateRequest request,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, request.getStatus(), email));
    }
}
