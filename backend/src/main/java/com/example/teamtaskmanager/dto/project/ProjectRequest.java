package com.example.teamtaskmanager.dto.project;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class ProjectRequest {
    @NotBlank
    @Size(max = 120)
    private String name;

    @Size(max = 1000)
    private String description;

    private Set<Long> memberIds;
}
