package com.example.teamtaskmanager.service;

import com.example.teamtaskmanager.dto.auth.AuthResponse;
import com.example.teamtaskmanager.dto.auth.LoginRequest;
import com.example.teamtaskmanager.dto.auth.RegisterRequest;

public interface AuthService {
    AuthResponse register(RegisterRequest request);
    AuthResponse login(LoginRequest request);
}
