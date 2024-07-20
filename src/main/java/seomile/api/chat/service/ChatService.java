package seomile.api.chat.service;

import org.springframework.stereotype.Service;
import seomile.api.chat.dto.ChatMessageDTO;
import seomile.api.chat.dto.ChatResponseDTO;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private List<ChatMessageDTO> messages = new ArrayList<>();

    // 모든 메시지 가져오기
    public List<ChatMessageDTO> getAllMessages() {
        return messages;
    }

    // 특정 사용자의 메시지 가져오기
    public List<ChatMessageDTO> getMessagesBySender(String sender) {
        return messages.stream()
                .filter(message -> message.getSender().equals(sender))
                .collect(Collectors.toList());
    }

    // 메시지 처리
    public ChatResponseDTO processMessage(ChatMessageDTO chatMessageDTO) {
        chatMessageDTO = new ChatMessageDTO(
                chatMessageDTO.getSender(),
                chatMessageDTO.getRecipientBot(),
                chatMessageDTO.getMessage()
        );
        messages.add(chatMessageDTO);
        ChatResponseDTO response = new ChatResponseDTO(
                "ChatBot", chatMessageDTO.getSender(), "응답 메시지", LocalDateTime.now()
        );
        return response;
    }
}