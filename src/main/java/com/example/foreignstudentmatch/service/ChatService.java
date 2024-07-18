package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.ChatMessage;
import com.example.foreignstudentmatch.domain.ChatRoom;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.StudentChatRoom;
import com.example.foreignstudentmatch.dto.chat.ChatDto;
import com.example.foreignstudentmatch.dto.chat.ChatRoomResponseDto;
import com.example.foreignstudentmatch.repository.ChatMessageRepository;
import com.example.foreignstudentmatch.repository.ChatRoomRepository;
import com.example.foreignstudentmatch.repository.StudentRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
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

        if (chatDto.getType().equals(ChatDto.MessageType.ENTER)) {
            sessionsPerRoom.computeIfAbsent(chatRoom.getId(), k -> new HashSet<>()).add(session);
            chatDto.setMessage(sender.getName() + " 님이 입장하였습니다.");
            sendMessageToRoom(chatRoom.getId(), chatDto);
        } else if (chatDto.getType().equals(ChatDto.MessageType.TALK)) {
            saveMessage(chatDto);
            sendMessageToRoom(chatRoom.getId(), chatDto);
        }
    }

    @Transactional
    public void saveMessage(ChatDto chatDto) {
        Student sender = studentRepository.findById(chatDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .student(sender)
                .message(chatDto.getMessage())
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

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> readChatRooms(Long studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        List<ChatRoom> chatRooms = student.getStudentChatRooms().stream()
                .map(StudentChatRoom::getChatRoom)
                .collect(Collectors.toList());

        List<ChatRoomResponseDto> responseDtos = chatRooms.stream().map(chatRoom -> {
                    ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomOrderByCreatedDateDesc(chatRoom);
                    String lastMessageContent = lastMessage != null ? lastMessage.getMessage() : null;
                    String lastMessageTime = lastMessage != null
                            ? lastMessage.getCreatedDate()
                            : chatRoom.getCreatedDate();

                    Student buddy = chatRoom.getStudentChatRooms().stream()
                            .map(StudentChatRoom::getStudent)
                            .filter(s -> !s.getId().equals(student.getId()))
                            .findFirst()
                            .orElseThrow(() -> new IllegalArgumentException("친구를 찾을 수 없습니다."));

                    return ChatRoomResponseDto.builder()
                            .chatRoomId(chatRoom.getId())
                            .buddyName(buddy.getName())
                            .buddyProfileImage(buddy.getProfileImage())
                            .lastMessage(lastMessageContent)
                            .lastMessageTime(lastMessageTime)
                            .build();
                }).sorted(Comparator.comparing(ChatRoomResponseDto::lastMessageTime).reversed())
                .collect(Collectors.toList());

        return responseDtos;
    }
}