package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.dto.mypage.MyPageResponseDto;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final StudentRepository studentRepository;

    @Transactional(readOnly = true)
    public MyPageResponseDto getStudentMyPage(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        return MyPageResponseDto.builder()
                .name(student.getName())
                .major(student.getMajor())
                .grade(student.getGrade())
                .profileImage(student.getProfileImage())
                .interestsKorean(student.getInterestsKorean())
                .interestsEnglish(student.getInterestsEnglish())
                .language(student.getLanguage())
                .buddyCount(0) // default 값
                .build();
    }
}
