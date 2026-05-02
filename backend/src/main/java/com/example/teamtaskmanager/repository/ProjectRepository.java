package com.example.teamtaskmanager.repository;

import com.example.teamtaskmanager.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    List<Project> findByTeamMembersId(Long memberId);
}
