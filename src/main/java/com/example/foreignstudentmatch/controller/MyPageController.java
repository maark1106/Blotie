package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.annotation.StudentId;
import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService MyPageService;

    @GetMapping("/{studentId}")
    public ResponseDto<?> getStudentMyPage(@StudentId Long id, @PathVariable("studentId") Long studentId) {
        return new ResponseDto<>(MyPageService.getStudentMyPage(studentId));
    }
}
