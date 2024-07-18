package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.StudentChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentChatRoomRepository extends JpaRepository<StudentChatRoom, Long> {

    @Query("SELECT scr FROM StudentChatRoom scr WHERE scr.student = :student1 AND scr.chatRoom IN (SELECT scr2.chatRoom FROM StudentChatRoom scr2 WHERE scr2.student = :student2)")
    Optional<StudentChatRoom> findByStudents(@Param("student1") Student student1, @Param("student2") Student student2);
}