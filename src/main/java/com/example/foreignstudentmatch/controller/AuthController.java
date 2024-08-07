// AuthController.java
package com.example.foreignstudentmatch.controller;

import com.example.foreignstudentmatch.dto.ResponseDto;
import com.example.foreignstudentmatch.dto.auth.AuthRequestDto;
import com.example.foreignstudentmatch.dto.auth.RegisterRequestDto;
import com.example.foreignstudentmatch.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth")
    public ResponseDto<?> auth(@RequestBody AuthRequestDto loginRequestDto) {
        return new ResponseDto<>(authService.auth(loginRequestDto));
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody RegisterRequestDto registerRequestDto) {
        return new ResponseDto<>(authService.register(registerRequestDto));
    }
}