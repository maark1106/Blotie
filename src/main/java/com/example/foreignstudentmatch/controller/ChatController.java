package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/chat")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping
    public ResponseDto<?> readChatRooms(@RequestParam("student_id") Long studentId){
        return new ResponseDto<>(chatService.readChatRooms(studentId));
    }
}
