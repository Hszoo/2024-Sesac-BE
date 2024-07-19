package seomile.api.member.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MemberRequestDTO {

    private Long id;
    private String memberId;
    private String memberPw;

}
