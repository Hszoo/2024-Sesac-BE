package seomile.api.chat.controller;

import org.springframework.web.bind.annotation.*;
import seomile.api.chat.dto.ChatResponseDTO;
import seomile.api.chat.dto.ChatMessageDTO;
import seomile.api.chat.service.ChatService;

import java.util.List;

@RestController
@RequestMapping("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    // 채팅 전체 조회
    @GetMapping("")
    public List<ChatMessageDTO> getMessages() {
        return chatService.getAllMessages();
    }

    // 특정 사용자 채팅 조회
    @GetMapping("/filter")
    public List<ChatMessageDTO> getMessagesBySender(@RequestParam String sender) {
        return chatService.getMessagesBySender(sender);
    }

    // 채팅 메시지 보내기
    @PostMapping("/send")
    public ChatResponseDTO sendMessage(@RequestBody ChatMessageDTO chatMessageDTO) {
        return chatService.processMessage(chatMessageDTO);
    }
}