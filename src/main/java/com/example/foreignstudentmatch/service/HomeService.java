package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.enums.MatchingStatus;
import com.example.foreignstudentmatch.dto.home.HomeResponseDto;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class HomeService {

    private final StudentRepository studentRepository;

    public HomeResponseDto getMatchingStatus(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        // Check if 3 days have passed since the last match
        if (student.getMatchingStatus() == MatchingStatus.MATCHED &&
                student.getMatchedAt().plusDays(3).isBefore(LocalDateTime.now())) {
            student.updateMatchStatus(MatchingStatus.AVAILABLE);
            studentRepository.save(student);
        }

        return HomeResponseDto.builder()
                .matchingStatus(student.getMatchingStatus())
                .matchedAt(student.getMatchedAt())
                .build();
    }
}
