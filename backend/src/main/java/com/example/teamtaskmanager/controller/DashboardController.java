package com.example.teamtaskmanager.controller;

import com.example.teamtaskmanager.dto.dashboard.DashboardResponse;
import com.example.teamtaskmanager.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/dashboard")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:5173}")
public class DashboardController {

    private final DashboardService dashboardService;

    @GetMapping("/my")
    public ResponseEntity<DashboardResponse> getMyDashboard(@AuthenticationPrincipal String email) {
        return ResponseEntity.ok(dashboardService.getMyDashboard(email));
    }
}
