package com.example.foreignstudentmatch.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
public class RegisterResponseDto {

    private Long studentId;
    private String accessToken;
    private String refreshToken;

    @Builder
    public RegisterResponseDto(Long studentId, String accessToken, String refreshToken) {
        this.studentId = studentId;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
    }
}
