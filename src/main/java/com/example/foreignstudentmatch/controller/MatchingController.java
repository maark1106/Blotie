package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.annotation.StudentId;
import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.service.MatchingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/match")
public class MatchingController {

    private final MatchingService matchingService;

    @PostMapping("")
    public ResponseDto<?> matchBuddy(@StudentId Long studentId){
        return new ResponseDto<>("200", matchingService.matchBuddy(studentId), null);
    }

    @DeleteMapping("")
    public ResponseDto<?> matchCancel(@StudentId Long studentId){
        return new ResponseDto<>("200", matchingService.matchCancel(studentId), null);
    }
}
