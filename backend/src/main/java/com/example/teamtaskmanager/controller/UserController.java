package com.example.teamtaskmanager.controller;

import com.example.teamtaskmanager.dto.UserSummaryResponse;
import com.example.teamtaskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "${app.cors.allowed-origins:http://localhost:5173}")
public class UserController {
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<UserSummaryResponse>> getUsers() {
        List<UserSummaryResponse> users = userRepository.findAll()
                .stream()
                .map(user -> new UserSummaryResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getRole()
                ))
                .toList();
        return ResponseEntity.ok(users);
    }
}
