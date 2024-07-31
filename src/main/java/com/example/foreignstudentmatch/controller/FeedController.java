package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.annotation.StudentId;
import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.dto.auth.RegisterResponseDto;
import com.example.foreignstudentmatch.dto.feed.FeedRequestDto;
import com.example.foreignstudentmatch.dto.feed.FeedUpdateRequestDto;
import com.example.foreignstudentmatch.service.FeedService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController {

    private final FeedService feedService;

    @PostMapping
    public ResponseDto<?> saveFeed(
            @StudentId Long studentId,
            @ModelAttribute FeedRequestDto feedRequestDto
    ) throws IOException {
        return new ResponseDto<>(feedService.saveFeed(studentId, feedRequestDto));
    }

    @GetMapping
    public ResponseDto<?> getFeeds(@RequestParam(value = "page", defaultValue = "1") int page,
                                   @StudentId Long studentId) {
        return new ResponseDto<>(feedService.getFeeds(page, studentId));
    }

    @GetMapping("/likes")
    public ResponseDto<?> getFeedsByLikes(@RequestParam(value = "page", defaultValue = "1") int page,
                                          @StudentId Long studentId) {
        return new ResponseDto<>(feedService.getFeedsByLikes(page, studentId));
    }

    @GetMapping("/student")
    public ResponseDto<?> getFeedsByStudent(@RequestParam(value = "page", defaultValue = "1") int page,
                                            @StudentId Long studentId) {
        return new ResponseDto<>(feedService.getFeedsByStudent(page, studentId));
    }

    @GetMapping("/{feed_id}")
    public ResponseDto<?> getFeed(@PathVariable("feed_id") Long feedId,
                                  @StudentId Long studentId) {
        return new ResponseDto<>(feedService.getFeed(feedId, studentId));
    }

    @PatchMapping("/{feed_id}")
    public ResponseDto<?> updateFeed(@PathVariable("feed_id") Long feedId,
                                     @StudentId Long studentId,
                                     @RequestBody FeedUpdateRequestDto requestDto) {
        return new ResponseDto<>(feedService.updateFeed(feedId, studentId, requestDto));
    }

    @DeleteMapping("/{feed_id}")
    public ResponseDto<?> deleteFeed(@PathVariable("feed_id") Long feedId, @StudentId Long studentId) {
        feedService.deleteFeed(feedId, studentId);
        return new ResponseDto<>("200", "피드가 삭제되었습니다", null);
    }


}
