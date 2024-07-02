package com.example.foreignstudentmatch.dto.feed;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class FeedListResponseDto {
    private List<FeedResponseDto> feeds;
    private int currentPage;
    private int totalPage;
}