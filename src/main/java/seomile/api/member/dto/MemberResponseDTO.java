package seomile.api.member.dto;
import lombok.*;
import seomile.api.member.entity.Member;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    private Long id;
    private String memberId;

    public MemberResponseDTO(Member member) {
        this.id = member.getId();
        this.memberId = member.getMemberId();
    }

}