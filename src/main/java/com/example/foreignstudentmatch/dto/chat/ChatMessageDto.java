package com.example.foreignstudentmatch.dto.chat;

import com.example.foreignstudentmatch.domain.ChatMessage;
import lombok.Builder;

public record ChatMessageDto(
        String sender,
        String content,
        String createdDate
) {

    @Builder
    public ChatMessageDto(String sender, String content, String createdDate) {
        this.sender = sender;
        this.content = content;
        this.createdDate = createdDate;
    }

    public static ChatMessageDto of(ChatMessage chatMessage){
        return ChatMessageDto.builder()
                .sender(chatMessage.getStudent().getName())
                .content(chatMessage.getMessage())
                .createdDate(chatMessage.getCreatedDate())
                .build();
    }
}
