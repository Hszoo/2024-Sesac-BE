package seomile.api.chat.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seomile.api.chat.dto.ChatResponseDTO;
import seomile.api.chat.dto.ChatMessageDTO;
import seomile.api.chat.service.ChatService;

import java.util.List;
@RestController
@RequiredArgsConstructor
@RequestMapping("/chat")
public class ChatController {
    private final ChatService chatService;

    // 채팅 전체 조회
    @GetMapping
    public List<ChatMessageDTO> getMessages() {
        return chatService.getAllMessages();
    }

    // 특정 사용자 채팅방 조회
    @GetMapping("/filter")
    public List<ChatMessageDTO> getMessagesBySender(@RequestParam String sender) {
        return chatService.getMessagesBySender(sender);
    }

    // 채팅 메시지 보내기
    @PostMapping("/send")
    public ResponseEntity<ChatResponseDTO> sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        ChatResponseDTO response = chatService.processMessage(chatMessageDTO);
        return ResponseEntity.ok(response);
    }

    //응답 메시지 읽기
    @GetMapping("/response")
    public List<ChatResponseDTO> getResponses() {
        return chatService.getAllResponses();
    }

}