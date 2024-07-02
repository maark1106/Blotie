package com.example.foreignstudentmatch.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FeedCommentResponseDto {
    private Long commentId;
    private String createdDate;
    private int commentNumber;
    private String profileImage;
    private String content;
}
