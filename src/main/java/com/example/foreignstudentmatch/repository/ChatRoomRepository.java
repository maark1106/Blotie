package com.example.foreignstudentmatch.repository;

import com.example.foreignstudentmatch.domain.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
}
