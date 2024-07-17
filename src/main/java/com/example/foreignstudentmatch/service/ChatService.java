package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.ChatMessage;
import com.example.foreignstudentmatch.domain.ChatRoom;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.dto.chat.ChatDto;
import com.example.foreignstudentmatch.repository.ChatMessageRepository;
import com.example.foreignstudentmatch.repository.ChatRoomRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Slf4j
@Data
@Service
public class ChatService {

    private final ObjectMapper mapper;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final StudentRepository studentRepository;
    private final Map<Long, Set<WebSocketSession>> sessionsPerRoom = new HashMap<>();

    @PostConstruct
    private void init() {
        // 초기화 작업
    }

    public <T> void sendMessage(WebSocketSession session, T message) {
        try {
            session.sendMessage(new TextMessage(mapper.writeValueAsString(message)));
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        }
    }

    public void handleAction(WebSocketSession session, ChatDto chatDto) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        Student sender = studentRepository.findById(chatDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        // 처음 들어왔을 때(ENTER) 이면 채팅방에 대한 사용자의 session을 추가
        if (chatDto.getType().equals(ChatDto.MessageType.ENTER)) {
            sessionsPerRoom.computeIfAbsent(chatRoom.getId(), k -> new HashSet<>()).add(session);
            chatDto.setMessage(sender.getName() + " 님이 입장하였습니다.");
            sendMessageToRoom(chatRoom.getId(), chatDto);
        } // 메세지를 보내면(TALK) DB에 메세지 저장하고 해당 채팅방에 참여한 세션에게 메세지 보내기
        else if (chatDto.getType().equals(ChatDto.MessageType.TALK)) {
            saveMessage(chatDto);
            System.out.println(chatDto.getMessage());
            sendMessageToRoom(chatRoom.getId(), chatDto);
        }
    }

    public void saveMessage(ChatDto chatDto) {
        Student sender = studentRepository.findById(chatDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .student(sender)
                .message(chatDto.getMessage())
                .timestamp(LocalDateTime.now())
                .build();
        chatMessageRepository.save(chatMessage);
    }

    public <T> void sendMessageToRoom(Long roomId, T message) {
        Set<WebSocketSession> sessions = sessionsPerRoom.get(roomId);
        if (sessions != null) {
            sessions.parallelStream().forEach(session -> sendMessage(session, message));
        }
    }

    public void removeSession(WebSocketSession session) {
        sessionsPerRoom.values().forEach(sessions -> sessions.remove(session));
    }
}
