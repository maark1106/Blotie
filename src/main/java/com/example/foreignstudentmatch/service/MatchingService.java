package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.*;
import com.example.foreignstudentmatch.domain.enums.MatchingStatus;
import com.example.foreignstudentmatch.domain.enums.Nationality;
import com.example.foreignstudentmatch.repository.ChatRoomRepository;
import com.example.foreignstudentmatch.repository.MatchingRepository;
import com.example.foreignstudentmatch.repository.StudentChatRoomRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchingService {

    private final StudentRepository studentRepository;
    private final MatchingRepository matchingRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final StudentChatRoomRepository studentChatRoomRepository;

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
            student.updateMatchStatus(MatchingStatus.PENDING);
            studentRepository.save(student);
            return "매칭이 요청되었습니다";
        } else {
            MatchingRequest matching = matchings.get(0);
            ChatRoom chatRoom = createChatRoom(student, matching.getStudent());
            if(chatRoom == null){
                return "이미 존재하는 매칭입니다";
            }
            chatRoomRepository.save(chatRoom);
            studentChatRoomRepository.save(StudentChatRoom.builder().student(student).chatRoom(chatRoom).build());
            studentChatRoomRepository.save(StudentChatRoom.builder().student(matching.getStudent()).chatRoom(chatRoom).build());
            matchingRepository.delete(matching);

            student.updateMatchStatus(MatchingStatus.MATCHED);
            studentRepository.save(student);

            Student buddy = matching.getStudent();
            buddy.updateMatchStatus(MatchingStatus.MATCHED);
            studentRepository.save(buddy);

            return "매칭되었습니다.";
        }
    }

    private ChatRoom createChatRoom(Student student1, Student student2) {
        Optional<StudentChatRoom> chatRoomOptional = studentChatRoomRepository.findByStudents(student1, student2);
        if (chatRoomOptional.isPresent()) {
            return null;
        }

        return new ChatRoom();
    }
}
