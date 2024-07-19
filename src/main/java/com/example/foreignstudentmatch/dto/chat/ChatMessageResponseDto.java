package com.example.foreignstudentmatch.dto.chat;

import lombok.Builder;

import java.util.List;

public record ChatMessageResponseDto(
        String profileImage,
        Long buddyId,
        String buddyName,
        List<ChatMessageDto> messages
) {
    @Builder
    public ChatMessageResponseDto(String profileImage, Long buddyId, String buddyName, List<ChatMessageDto> messages) {
        this.profileImage = profileImage;
        this.buddyId = buddyId;
        this.buddyName = buddyName;
        this.messages = messages;
    }
}
