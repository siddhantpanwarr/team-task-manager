package com.example.teamtaskmanager.service.impl;

import com.example.teamtaskmanager.dto.dashboard.DashboardResponse;
import com.example.teamtaskmanager.entity.TaskStatus;
import com.example.teamtaskmanager.entity.User;
import com.example.teamtaskmanager.exception.ResourceNotFoundException;
import com.example.teamtaskmanager.repository.TaskRepository;
import com.example.teamtaskmanager.repository.UserRepository;
import com.example.teamtaskmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public DashboardResponse getMyDashboard(String userEmail) {
        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        long total = taskRepository.countByAssignedUserId(user.getId());
        long completed = taskRepository.countByAssignedUserIdAndStatus(user.getId(), TaskStatus.DONE);
        long pending = taskRepository.countByAssignedUserIdAndStatusNot(user.getId(), TaskStatus.DONE);
        long overdue = taskRepository.countByAssignedUserIdAndStatusNotAndDueDateBefore(
                user.getId(), TaskStatus.DONE, LocalDate.now()
        );
        return new DashboardResponse(total, completed, pending, overdue);
    }
}
