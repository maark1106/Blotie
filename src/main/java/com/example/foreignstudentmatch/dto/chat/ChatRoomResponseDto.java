package com.example.foreignstudentmatch.dto.chat;

import lombok.Builder;
import lombok.Getter;

public record ChatRoomResponseDto(
        Long chatRoomId,
        String buddyName,
        String buddyProfileImage,
        String lastMessage,
        String lastMessageTime
) {

    @Builder
    public ChatRoomResponseDto(Long chatRoomId, String buddyName, String buddyProfileImage, String lastMessage, String lastMessageTime) {
        this.chatRoomId = chatRoomId;
        this.buddyName = buddyName;
        this.buddyProfileImage = buddyProfileImage;
        this.lastMessage = lastMessage;
        this.lastMessageTime = lastMessageTime;
    }
}
