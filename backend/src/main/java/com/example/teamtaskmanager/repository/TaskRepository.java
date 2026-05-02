package com.example.teamtaskmanager.repository;

import com.example.teamtaskmanager.entity.Task;
import com.example.teamtaskmanager.entity.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssignedUserId(Long userId);
    long countByAssignedUserId(Long userId);
    long countByAssignedUserIdAndStatus(Long userId, TaskStatus status);
    long countByAssignedUserIdAndStatusNot(Long userId, TaskStatus status);
    long countByAssignedUserIdAndStatusNotAndDueDateBefore(Long userId, TaskStatus status, LocalDate dueDate);
}
