package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.annotation.StudentId;
import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/{feed_id}")
    public ResponseDto<?> addLike(@StudentId Long studentId, @PathVariable("feed_id") Long feedId) {
        likeService.addLike(studentId, feedId);
        return new ResponseDto<>("200", "이 피드를 좋아합니다", null);
    }

    @DeleteMapping("/{feed_id}")
    public ResponseDto<?> removeLike(@StudentId Long studentId, @PathVariable("feed_id") Long feedId) {
        likeService.removeLike(studentId, feedId);
        return new ResponseDto<>("200", "좋아요를 취소합니다", null);
    }
}
