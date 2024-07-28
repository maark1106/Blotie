package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.RefreshToken;
import com.example.foreignstudentmatch.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByStudent(Student student);

    Optional<RefreshToken> findByStudentId(Long studentId);
}
