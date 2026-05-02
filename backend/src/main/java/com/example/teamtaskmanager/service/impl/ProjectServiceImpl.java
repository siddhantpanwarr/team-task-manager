package com.example.teamtaskmanager.service.impl;

import com.example.teamtaskmanager.dto.project.ProjectRequest;
import com.example.teamtaskmanager.dto.project.ProjectResponse;
import com.example.teamtaskmanager.entity.Project;
import com.example.teamtaskmanager.entity.Role;
import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.exception.BadRequestException;
import com.example.teamtaskmanager.exception.ResourceNotFoundException;
import com.example.teamtaskmanager.repository.ProjectRepository;
import com.example.teamtaskmanager.repository.UserRepository;
import com.example.teamtaskmanager.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectResponse createProject(ProjectRequest request, String adminEmail) {
        User admin = getUser(adminEmail);
        assertAdmin(admin);
        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setCreatedBy(admin);
        project.setTeamMembers(resolveMembers(request.getMemberIds()));
        project.getTeamMembers().add(admin);
        return toResponse(projectRepository.save(project));
    }

    @Override
    public List<ProjectResponse> getProjectsForCurrentUser(String email) {
        User current = getUser(email);
        List<Project> projects = current.getRole() == Role.ROLE_ADMIN
                ? projectRepository.findAll()
                : projectRepository.findByTeamMembersId(current.getId());
        return projects.stream().map(this::toResponse).toList();
    }

    @Override
    public ProjectResponse updateProject(Long projectId, ProjectRequest request, String adminEmail) {
        User admin = getUser(adminEmail);
        assertAdmin(admin);
        Project project = getProject(projectId);
        if (!project.getCreatedBy().getId().equals(admin.getId())) {
            throw new BadRequestException("Only project creator can update this project");
        }
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        Set<User> members = resolveMembers(request.getMemberIds());
        members.add(admin);
        project.setTeamMembers(members);
        return toResponse(projectRepository.save(project));
    }

    @Override
    public void deleteProject(Long projectId, String adminEmail) {
        User admin = getUser(adminEmail);
        assertAdmin(admin);
        Project project = getProject(projectId);
        if (!project.getCreatedBy().getId().equals(admin.getId())) {
            throw new BadRequestException("Only project creator can delete this project");
        }
        projectRepository.delete(project);
    }

    private Set<User> resolveMembers(Set<Long> memberIds) {
        if (memberIds == null || memberIds.isEmpty()) {
            return new HashSet<>();
        }
        return memberIds.stream()
                .map(id -> userRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id)))
                .collect(Collectors.toSet());
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

    private ProjectResponse toResponse(Project project) {
        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .createdById(project.getCreatedBy().getId())
                .createdByName(project.getCreatedBy().getFullName())
                .createdAt(project.getCreatedAt())
                .members(project.getTeamMembers().stream().map(member ->
                        ProjectResponse.MemberSummary.builder()
                                .id(member.getId())
                                .fullName(member.getFullName())
                                .email(member.getEmail())
                                .build()
                ).collect(Collectors.toSet()))
                .build();
    }
}
