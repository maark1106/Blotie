package com.example.foreignstudentmatch.config;

import com.example.foreignstudentmatch.dto.chat.ChatDto;
import com.example.foreignstudentmatch.service.ChatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
@Slf4j
@RequiredArgsConstructor
public class ChatHandler extends TextWebSocketHandler {

    private final ObjectMapper mapper;
    private final ChatService service;

    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = (String) message.getPayload();
        log.info("payload : {}", payload);

        // JSON -> Java Object
        ChatDto chatMessage = mapper.readValue(payload, ChatDto.class);
        log.info("session : {}", chatMessage.toString());

        service.handleAction(session, chatMessage);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        log.info(session + " 클라이언트 접속");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info(session + " 클라이언트 접속 해제");
        // 세션 종료 처리
        service.removeSession(session);
    }
}
