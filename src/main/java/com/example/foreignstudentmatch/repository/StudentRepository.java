package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long>{
    Optional<Student> findByStudentNumber(String studentNumber);
}
