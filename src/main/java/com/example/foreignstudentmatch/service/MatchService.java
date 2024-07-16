package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.ChatRoom;
import com.example.foreignstudentmatch.domain.MatchingRequest;
import com.example.foreignstudentmatch.domain.Nationality;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.repository.ChatRoomRepository;
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
    private final ChatRoomRepository chatRoomRepository;

    @Transactional
    public String matchBuddy(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));
        Nationality nationality = student.isKorean() ? Nationality.FOREIGNER : Nationality.KOREAN;

        List<MatchingRequest> matchings = matchingRepository.findByNationality(nationality);

        // 매칭할 사람이 없다면 MatchingRequest 생성 후 저장
        if (matchings.isEmpty()) {
            MatchingRequest newMatching = MatchingRequest.builder()
                    .student(student)
                    .nationality(student.isKorean() ? Nationality.KOREAN : Nationality.FOREIGNER)
                    .build();
            matchingRepository.save(newMatching);
            return "매칭 결과가 없습니다.";
        } else { // 매칭할 사람이 있다면 채팅방(roomId) 생성 후 매칭 요청 삭제
            MatchingRequest matching = matchings.get(0);

            ChatRoom chatRoom = createChatRoom(student, matching);
            chatRoomRepository.save(chatRoom);

            matchingRepository.delete(matching);
            return "매칭되었습니다.";
        }
    }

    private ChatRoom createChatRoom(Student student, MatchingRequest matching) {
        Student koreanStudent = student.isKorean() ? student : matching.getStudent();
        Student foreignStudent = student.isKorean() ? matching.getStudent() : student;

        return ChatRoom.builder()
                .koreanStudent(koreanStudent)
                .foreignStudent(foreignStudent)
                .build();
    }
}
