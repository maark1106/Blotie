package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.MatchingRequest;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.enums.Nationality;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MatchingRepository extends JpaRepository<MatchingRequest, Long> {
    List<MatchingRequest> findByNationality(Nationality nationality);
    Optional<MatchingRequest> findByStudent(Student student);
}
