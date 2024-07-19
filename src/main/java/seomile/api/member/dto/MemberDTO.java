package seomile.api.member.dto;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {
    private Long id;
    private String user_id;
    private String user_pw;
}