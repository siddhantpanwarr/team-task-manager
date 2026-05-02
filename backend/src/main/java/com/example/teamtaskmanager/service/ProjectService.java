package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.dto.project.ProjectRequest;
import com.example.teamtaskmanager.dto.project.ProjectResponse;

import java.util.List;

public interface ProjectService {
    ProjectResponse createProject(ProjectRequest request, String adminEmail);
    List<ProjectResponse> getProjectsForCurrentUser(String email);
    ProjectResponse updateProject(Long projectId, ProjectRequest request, String adminEmail);
    void deleteProject(Long projectId, String adminEmail);
}
