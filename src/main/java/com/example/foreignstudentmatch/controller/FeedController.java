package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.dto.auth.RegisterResponseDto;
import com.example.foreignstudentmatch.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping("/save")
    public ResponseDto<?> saveFeed(
            @RequestParam("studentId") Long studentId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("images") List<MultipartFile> images
    ) throws IOException {
        return new ResponseDto<>(feedService.saveFeed(studentId, title, content, images));
    }

    @GetMapping
    public ResponseDto<?> getFeeds(@RequestParam(value = "page", defaultValue = "1") int page) {
        return new ResponseDto<>(feedService.getFeeds(page));
    }
}
