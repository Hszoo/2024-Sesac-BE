package seomile.api.chat.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import seomile.api.chat.dto.ChatResponseDTO;
import seomile.api.chat.dto.ChatMessageDTO;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Component
public class ChatHandler extends TextWebSocketHandler {

    private Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        sessions.put(session.getId(), session);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 메시지 처리 로직
        ChatMessageDTO chatMessage = new ObjectMapper().readValue(message.getPayload(), ChatMessageDTO.class);
        ChatResponseDTO response = new ChatResponseDTO("ChatBot", chatMessage.getSender(), "응답 메시지", LocalDateTime.now());

        TextMessage responseMessage = new TextMessage(new ObjectMapper().writeValueAsString(response));
        session.sendMessage(responseMessage);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        sessions.remove(session.getId());
    }
}
