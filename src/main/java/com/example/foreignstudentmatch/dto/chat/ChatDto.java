package com.example.foreignstudentmatch.dto.chat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Data
@NoArgsConstructor
public class ChatDto {

    public enum MessageType {
        ENTER, TALK
    }

    private MessageType type;  // 메시지 타입 (입장, 채팅)
    private Long roomId;     // 방 번호
    private Long studentId;    // 메시지 발송자 ID
    private String message;    // 메시지 내용
    private String time;       // 메시지 발송 시간

    @Builder
    public ChatDto(MessageType type, Long roomId, Long studentId, String message) {
        this.type = type;
        this.roomId = roomId;
        this.studentId = studentId;
        this.message = message;
        this.time = new Timestamp(System.currentTimeMillis()).toString();
    }

    public void setTime(String time) {
        this.time = time;
    }
}