package com.example.teamtaskmanager.dto.project;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Builder
public class ProjectResponse {
    private Long id;
    private String name;
    private String description;
    private Long createdById;
    private String createdByName;
    private Set<MemberSummary> members;
    private LocalDateTime createdAt;

    @Getter
    @Builder
    public static class MemberSummary {
        private Long id;
        private String fullName;
        private String email;
    }
}
