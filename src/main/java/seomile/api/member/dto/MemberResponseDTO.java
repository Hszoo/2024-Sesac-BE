package seomile.api.member.dto;

import lombok.*;
import seomile.api.member.entity.Authority;
import seomile.api.member.entity.Member;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDTO {
    private Long id;
    private String username;
    private List<Authority> roles = new ArrayList<>();

    private String token;

    public MemberResponseDTO(Member member) {
        this.id = member.getId();
        this.username = member.getUsername();
        this.roles = member.getRoles();
    }

}