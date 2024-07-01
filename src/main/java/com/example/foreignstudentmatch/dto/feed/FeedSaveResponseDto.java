package com.example.foreignstudentmatch.dto.feed;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FeedSaveResponseDto {
    private Long feedId;

    @Builder
    public FeedSaveResponseDto(Long feedId) {
        this.feedId = feedId;
    }
}
