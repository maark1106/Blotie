package com.example.foreignstudentmatch.dto.feed;

import lombok.Getter;

@Getter
public class FeedSaveResponseDto {
    private Long feedId;

    public FeedSaveResponseDto(Long feedId) {
        this.feedId = feedId;
    }
}
