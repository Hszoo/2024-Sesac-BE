package seomile.api.member.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import seomile.api.member.dto.MemberRequestDTO;
import seomile.api.member.dto.MemberResponseDTO;
import seomile.api.member.entity.Member;
import seomile.api.member.repository.MemberRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public String login(MemberRequestDTO request) throws Exception {
        Member member = memberRepository.findByMemberId(request.getMemberId())
                .orElseThrow(() -> new BadCredentialsException("잘못된 계정정보입니다."));

        if (!passwordEncoder.matches(request.getMemberPw(), member.getMemberPw())) {
            throw new BadCredentialsException("잘못된 계정정보입니다.");
        }

        return member.getMemberId() + " 로그인 성공";
    }

    public String signup(MemberRequestDTO request) throws Exception {
        try {
            Member member = Member.builder()
                    .memberId(request.getMemberId())
                    .memberPw(passwordEncoder.encode(request.getMemberPw()))
                    .build();

            memberRepository.save(member);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new Exception("잘못된 요청입니다.");
        }
        return request.getMemberId() + " 회원가입 성공";
    }

    public MemberResponseDTO getMember(String memberId) throws Exception {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new Exception("계정을 찾을 수 없습니다."));
        return new MemberResponseDTO(member);
    }

    public List<MemberResponseDTO> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(MemberResponseDTO::new)
                .collect(Collectors.toList());
    }

}