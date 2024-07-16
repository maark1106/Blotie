package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.MatchingRequest;
import com.example.foreignstudentmatch.domain.Nationality;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.repository.MatchingRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MatchService {

    private final StudentRepository studentRepository;
    private final MatchingRepository matchingRepository;

    @Transactional
    public String matchBuddy(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
        Nationality nationality = student.isKorean() ? Nationality.FOREIGNER : Nationality.KOREAN;

        List<MatchingRequest> matchings = matchingRepository.findByNationality(nationality);
        if (matchings.isEmpty()) {
            MatchingRequest newMatching = MatchingRequest.builder()
                    .student(student)
                    .nationality(student.isKorean() ? Nationality.KOREAN : Nationality.FOREIGNER)
                    .build();
            matchingRepository.save(newMatching);
            return "매칭 결과가 없습니다.";
        } else {
            MatchingRequest matching = matchings.get(0);

            return "매칭되었습니다.";
        }
    }
}
