package com.example.foreignstudentmatch.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FeedResponseDto {
    private Long feedId;
    private String createdDate;
    private String feedName;
    private String profileImage;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
}
