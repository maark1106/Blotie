package com.example.foreignstudentmatch.dto.mypage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MyPageResponseDto {
    private String name;
    private String major;
    private int grade;
    private String profileImage;
    private List<String> interestsKorean;
    private List<String> interestsEnglish;
    private List<String> language;
    private int buddyCount;
}
