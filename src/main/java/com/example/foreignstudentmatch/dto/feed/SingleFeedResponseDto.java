package com.example.foreignstudentmatch.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class SingleFeedResponseDto {
    private Long feedId;
    private String createdDate;
    private String profileImage;
    private Long studentId;
    private String title;
    private String content;
    private List<String> images;
    private boolean isLike;
    private int commentsCount;
    private List<FeedCommentResponseDto> comments;
}
