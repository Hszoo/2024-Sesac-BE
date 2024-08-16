package seomile.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import seomile.api.member.dto.MemberRequestDTO;
import seomile.api.member.dto.MemberResponseDTO;
import seomile.api.member.entity.Authority;
import seomile.api.member.entity.Member;
import seomile.api.member.repository.MemberRepository;
import seomile.api.security.JwtProvider;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    @Transactional
    public Member signup(MemberRequestDTO request) throws Exception {
        try {
            Member member = Member.builder()
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            member.setRoles(Collections.singletonList(Authority.builder().name("ROLE_USER").build()));

            memberRepository.save(member);
            return member;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
    }

    public MemberResponseDTO login(MemberRequestDTO request) throws Exception {
        Member member = memberRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));

        if (!passwordEncoder.matches(request.getPassword(), member.getPassword())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        if (member.getRoles() == null || member.getRoles().isEmpty()) {
            throw new Exception("사용자의 역할이 설정되지 않았습니다.");
        }

        return MemberResponseDTO.builder()
                .id(member.getId())
                .username(member.getUsername())
                .token(jwtProvider.createToken(member.getUsername(), member.getRoles()))
                .build();
    }

    @Transactional(readOnly = true)
    public Member getMemberFromToken(String token) throws Exception {
        // 토큰 검증 및 사용자 계정 추출
        if (!jwtProvider.validateToken(token)) {
            throw new Exception("유효하지 않은 토큰입니다.");
        }

        String username = jwtProvider.getAccount(token);

        // 사용자 정보 조회
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
    }
}