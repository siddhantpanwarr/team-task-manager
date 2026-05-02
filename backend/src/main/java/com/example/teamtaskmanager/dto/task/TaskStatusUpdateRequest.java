package com.example.teamtaskmanager.dto.task;

import com.example.teamtaskmanager.entity.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskStatusUpdateRequest {
    @NotNull
    private TaskStatus status;
}
