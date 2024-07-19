package seomile.api.chat.dto;

import java.util.Date;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatDTO {
    private Long chatId;
    private Long userId;
    private String content;
    private Date date;
    private String request;
}