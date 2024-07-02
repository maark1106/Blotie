package com.example.foreignstudentmatch.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class FeedListResponseDto {
    private List<FeedResponseDto> feeds;
    private int currentPage;
    private int totalPage;
}