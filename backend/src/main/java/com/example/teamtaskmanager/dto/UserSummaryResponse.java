package com.example.teamtaskmanager.dto;

import com.example.teamtaskmanager.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSummaryResponse {
    private Long id;
    private String fullName;
    private String email;
    private Role role;
}
