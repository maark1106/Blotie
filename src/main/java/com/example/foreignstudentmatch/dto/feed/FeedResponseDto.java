package com.example.foreignstudentmatch.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedResponseDto {
    private Long feedId;
    private String createdDate;
    private String profileImage;
    private String title;
    private String content;
    private int likeCount;
    private int commentCount;
    private boolean isLike;
}
