package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.dto.dashboard.DashboardResponse;

public interface DashboardService {
    DashboardResponse getMyDashboard(String userEmail);
}
