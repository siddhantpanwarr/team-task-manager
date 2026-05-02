package com.example.teamtaskmanager.dto.task;

import com.example.teamtaskmanager.entity.TaskStatus;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
public class TaskResponse {
    private Long id;
    private String title;
    private String description;
    private TaskStatus status;
    private LocalDate dueDate;
    private Long projectId;
    private String projectName;
    private Long assignedUserId;
    private String assignedUserName;
    private LocalDateTime createdAt;
}
