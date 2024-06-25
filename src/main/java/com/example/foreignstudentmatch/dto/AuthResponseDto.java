package com.example.foreignstudentmatch.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class AuthResponseDto {
    private String grade;
    private String major;
    private String name;
    private boolean isJoined;
    private String accessToken;

    @Builder
    public AuthResponseDto(String grade, String major, String name, boolean isJoined, String accessToken) {
        this.grade = grade;
        this.major = major;
        this.name = name;
        this.isJoined = isJoined;
        this.accessToken = accessToken;
    }
}
