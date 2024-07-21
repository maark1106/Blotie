package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.ChatRoom;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.StudentChatRoom;
import com.example.foreignstudentmatch.dto.mypage.MyPageResponseDto;
import com.example.foreignstudentmatch.repository.ChatRoomRepository;
import com.example.foreignstudentmatch.repository.StudentChatRoomRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class MyPageService {

    private final StudentRepository studentRepository;
    private final StudentChatRoomRepository studentChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;

    @Transactional(readOnly = true)
    public MyPageResponseDto getStudentMyPage(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        return MyPageResponseDto.of(student, getBuddies(studentId));
    }

    private List<Long> getBuddies(Long studentId) {
        List<ChatRoom> chatRooms = studentChatRoomRepository.findByStudentId(studentId)
                .stream()
                .map(StudentChatRoom::getChatRoom)
                .collect(Collectors.toList());

        return studentChatRoomRepository.findByChatRoomIn(chatRooms).stream()
                .map(StudentChatRoom::getStudent)
                .filter(student -> !student.getId().equals(studentId))
                .map(Student::getId)
                .collect(Collectors.toList());
    }
}
