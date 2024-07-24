package com.example.foreignstudentmatch.dto.home;

import com.example.foreignstudentmatch.domain.enums.MatchingStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record HomeResponseDto(
        MatchingStatus matchingStatus,
        String matchedAt
) {

    @Builder
    public static HomeResponseDto of(MatchingStatus matchingStatus, LocalDateTime matchedAt) {
        return new HomeResponseDto(
                matchingStatus,
                matchedAt.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"))
        );
    }
}
