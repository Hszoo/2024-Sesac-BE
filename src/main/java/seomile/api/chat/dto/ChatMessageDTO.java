package seomile.api.chat.dto;

import java.time.LocalDateTime;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageDTO {
    private Long chatId;
    private String sender; // 질문하는 사용자
    private String recipientBot; // 질문받을 Bot
    private String message;
    private LocalDateTime timestamp;

    public ChatMessageDTO(String sender, String recipientBot, String message) {
        this.sender = sender;
        this.recipientBot = recipientBot;
        this.message = message;
        this.timestamp = LocalDateTime.now(); // 메시지 생성 시 자동으로 현재 시간 설정
    }
}