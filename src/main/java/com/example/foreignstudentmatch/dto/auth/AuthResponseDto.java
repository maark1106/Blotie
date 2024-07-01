package com.example.foreignstudentmatch.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthResponseDto {
    private String grade;
    private String major;
    private String name;
    private boolean isJoined;
    private String accessToken;
    private String refreshToken;

    @Builder
    public AuthResponseDto(String grade, String major, String name, boolean isJoined, String accessToken, String refreshToken) {
        this.grade = grade;
        this.major = major;
        this.name = name;
        this.isJoined = isJoined;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
