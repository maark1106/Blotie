package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.dto.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/feed")
@RequiredArgsConstructor
public class FeedController{



    @PostMapping("/save")
    public ResponseDto<?> saveFeed(
            @RequestParam("studentId") Long studentId,
            @RequestParam("title") String title,
            @RequestParam("content") String content,
            @RequestParam("image") MultipartFile image
    ){

    }

}
