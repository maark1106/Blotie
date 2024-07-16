package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.ChatRoom;
import com.example.foreignstudentmatch.domain.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByKoreanStudentAndForeignStudent(Student koreanStudent, Student foreignStudent);
}