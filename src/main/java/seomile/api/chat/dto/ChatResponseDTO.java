package seomile.api.chat.dto;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatResponseDTO {
    private String bot;
    private String reciverUser; // 답변받을 사용자
    private String answer; // GPT 응답 메시지
    private LocalDateTime timestamp;

    public ChatResponseDTO(String reciverUser, String bot, String answer) {
        this.reciverUser = reciverUser;
        this.bot = bot;
        this.answer = answer;
        this.timestamp = LocalDateTime.now(); // 메시지 생성 시 자동으로 현재 시간 설정
    }
}
