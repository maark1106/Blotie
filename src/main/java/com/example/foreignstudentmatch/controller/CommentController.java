package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.annotation.StudentId;
import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.dto.comment.CommentSaveRequestDto;
import com.example.foreignstudentmatch.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{feed_id}/comment")
    public ResponseDto<?> saveComment(@PathVariable("feed_id") Long feedId, @RequestBody CommentSaveRequestDto commentSaveRequestDto, @StudentId Long studentId) {
        return new ResponseDto<>(commentService.saveComment(feedId, commentSaveRequestDto, studentId));
    }
}
