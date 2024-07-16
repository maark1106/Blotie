package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.service.MatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
public class MatchingController {

    private final MatchService matchService;

    @PostMapping("")
    public ResponseDto<?> matchBuddy(@RequestParam("student_id") Long studentId){
        return new ResponseDto<>(matchService.matchBuddy(studentId));
    }
}
