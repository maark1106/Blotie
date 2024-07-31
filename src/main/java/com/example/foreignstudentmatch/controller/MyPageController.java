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

    @GetMapping("/{id}")
    public ResponseDto<?> getStudentMyPage(@StudentId Long studentId, @PathVariable("id") Long id) {
        return new ResponseDto<>(MyPageService.getStudentMyPage(id));
    }
}
