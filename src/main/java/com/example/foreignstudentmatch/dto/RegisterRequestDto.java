package com.example.foreignstudentmatch.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequestDto {

    private String school;
    private String major;
    private String name;
    private String studentNumber;
    private int grade;
    private List<String> language;
    private String mbti;
    private List<String> interestsEnglish;
    private List<String> interestsKorean;
    private boolean isKorean;
}
