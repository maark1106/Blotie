package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.ChatMessage;
import com.example.foreignstudentmatch.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

    List<ChatMessage> findByChatRoom(ChatRoom chatRoom);

    ChatMessage findTopByChatRoomOrderByCreatedDateDesc(ChatRoom chatRoom);
}