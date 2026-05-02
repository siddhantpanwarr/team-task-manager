package com.example.teamtaskmanager.controller;

import com.example.teamtaskmanager.dto.project.ProjectRequest;
import com.example.teamtaskmanager.dto.project.ProjectResponse;
import com.example.teamtaskmanager.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:5173}")
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    public ResponseEntity<ProjectResponse> createProject(
            @Valid @RequestBody ProjectRequest request,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.status(HttpStatus.CREATED).body(projectService.createProject(request, email));
    }

    @GetMapping
    public ResponseEntity<List<ProjectResponse>> getProjects(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(projectService.getProjectsForCurrentUser(email));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProjectResponse> updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request,
            @AuthenticationPrincipal String email
    ) {
        return ResponseEntity.ok(projectService.updateProject(id, request, email));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id, @AuthenticationPrincipal String email) {
        projectService.deleteProject(id, email);
        return ResponseEntity.noContent().build();
    }
}
