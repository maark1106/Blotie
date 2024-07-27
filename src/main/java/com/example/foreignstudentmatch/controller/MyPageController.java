package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.annotation.StudentId;
import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mypage")
@RequiredArgsConstructor
public class MyPageController {

    private final MyPageService MyPageService;

    @GetMapping
    public ResponseDto<?> getStudentMyPage(@StudentId Long studentId) {
        return new ResponseDto<>(MyPageService.getStudentMyPage(studentId));
    }
}
