package com.example.foreignstudentmatch.service;

import com.example.foreignstudentmatch.domain.ChatMessage;
import com.example.foreignstudentmatch.domain.ChatRoom;
import com.example.foreignstudentmatch.domain.Student;
import com.example.foreignstudentmatch.domain.StudentChatRoom;
import com.example.foreignstudentmatch.dto.chat.ChatDto;
import com.example.foreignstudentmatch.dto.chat.ChatMessageDto;
import com.example.foreignstudentmatch.dto.chat.ChatMessageResponseDto;
import com.example.foreignstudentmatch.dto.chat.ChatRoomResponseDto;
import com.example.foreignstudentmatch.repository.ChatMessageRepository;
import com.example.foreignstudentmatch.repository.ChatRoomRepository;
import com.example.foreignstudentmatch.repository.StudentChatRoomRepository;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final StudentChatRoomRepository studentChatRoomRepository;
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

    public void saveMessage(ChatDto chatDto) {
        Student sender = studentRepository.findById(chatDto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException("학생을 찾을 수 없습니다."));

        ChatRoom chatRoom = chatRoomRepository.findById(chatDto.getRoomId())
                .orElseThrow(() -> new IllegalArgumentException("채팅방을 찾을 수 없습니다."));

        ChatMessage chatMessage = ChatMessage.builder()
                .chatRoom(chatRoom)
                .student(sender)
                .message(chatDto.getMessage())
                .createdDate(LocalDateTime.now())
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
        // 주어진 studentId로 학생을 조회하고, 존재하지 않으면 예외를 던짐
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 학생입니다."));

        // 학생이 참여한 모든 채팅방을 조회
        List<ChatRoom> chatRooms = student.getStudentChatRooms().stream()
                .map(StudentChatRoom::getChatRoom)
                .collect(Collectors.toList());

        // 각 채팅방을 ChatRoomResponseDto로 변환
        List<ChatRoomResponseDto> responseDtos = chatRooms.stream()
                .map(chatRoom -> createChatRoomResponseDto(chatRoom, student))
                .sorted(Comparator.comparing(ChatRoomResponseDto::lastMessageTime).reversed())
                .collect(Collectors.toList());
        return responseDtos;
    }

    private ChatRoomResponseDto createChatRoomResponseDto(ChatRoom chatRoom, Student student) {

        ChatMessage lastMessage = chatMessageRepository.findTopByChatRoomOrderByCreatedDateDesc(chatRoom);
        String lastMessageContent = (lastMessage != null) ? lastMessage.getMessage() : null;
        String lastMessageTime = (lastMessage != null) ? lastMessage.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")) : chatRoom.getCreatedDate();

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
    }

    @Transactional(readOnly = true)
    public ChatMessageResponseDto readChatMessages(Long chatRoomId, Long studentId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomIdOrderByCreatedDateAsc(chatRoomId);
        List<ChatMessageDto> chatMessageDtos = chatMessages.stream()
                .map(chatMessage -> ChatMessageDto.of(chatMessage))
                .collect(Collectors.toList());

        List<StudentChatRoom> studentChatRooms = studentChatRoomRepository.findByChatRoomId(chatRoomId);
        Student buddy = studentChatRooms.stream()
                .map(StudentChatRoom::getStudent)
                .filter(s -> !s.getId().equals(studentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("친구를 찾을 수 없습니다"));

        return ChatMessageResponseDto
                .builder()
                .profileImage(buddy.getProfileImage())
                .buddyId(buddy.getId())
                .buddyName(buddy.getName())
                .messages(chatMessageDtos)
                .build();
    }

    public String exitChatRoom(Long chatRoomId, Long studentId) {
        StudentChatRoom studentChatRoom = studentChatRoomRepository.findByStudentIdAndChatRoomId(studentId, chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅방에서 학생을 찾을 수 없습니다."));

        studentChatRoomRepository.delete(studentChatRoom);
        return "채팅방에서 나갔습니다.";
    }
}
