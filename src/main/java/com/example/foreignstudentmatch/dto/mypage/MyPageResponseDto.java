package com.example.foreignstudentmatch.dto.mypage;

import com.example.foreignstudentmatch.domain.Student;
import lombok.Builder;

import java.util.List;


public record MyPageResponseDto(
        String name,
        String major,
        int grade,
        boolean korean,
        String mbti,
        String profileImage,
        List<String> interestsKorean,
        List<String> interestsEnglish,
        List<String> language,
        int buddyCount,
        List<Long> buddies
) {
    @Builder
    public MyPageResponseDto(String name, String major, int grade, boolean korean, String mbti, String profileImage, List<String> interestsKorean, List<String> interestsEnglish, List<String> language, int buddyCount, List<Long> buddies) {
        this.name = name;
        this.major = major;
        this.grade = grade;
        this.korean = korean;
        this.mbti = mbti;
        this.profileImage = profileImage;
        this.interestsKorean = interestsKorean;
        this.interestsEnglish = interestsEnglish;
        this.language = language;
        this.buddyCount = buddyCount;
        this.buddies = buddies;
    }

    public static MyPageResponseDto of(Student student, List<Long> buddies){
        return MyPageResponseDto.builder()
                .name(student.getName())
                .major(student.getMajor())
                .grade(student.getGrade())
                .korean(student.isKorean())
                .mbti(student.getMbti())
                .profileImage(student.getProfileImage())
                .interestsKorean(student.getInterestsKorean())
                .interestsEnglish(student.getInterestsEnglish())
                .language(student.getLanguage())
                .buddies(buddies)
                .buddyCount(buddies.size())
                .build();
    }
}
