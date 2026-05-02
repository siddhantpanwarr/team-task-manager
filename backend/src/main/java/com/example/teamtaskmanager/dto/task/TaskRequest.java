package com.example.teamtaskmanager.dto.task;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class TaskRequest {
    @NotBlank
    @Size(max = 160)
    private String title;

    @Size(max = 2000)
    private String description;

    @NotNull
    @FutureOrPresent
    private LocalDate dueDate;

    @NotNull
    private Long assignedUserId;
}
