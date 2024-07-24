package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.enums.MatchingStatus;
import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.dto.home.HomeResponseDto;
import com.example.foreignstudentmatch.repository.StudentRepository;
import com.example.foreignstudentmatch.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;

    @GetMapping
    public ResponseDto<?> getHome(@RequestParam("student_id") Long studentId) {
        return new ResponseDto<>(homeService.getMatchingStatus(studentId));
    }
}