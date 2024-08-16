package seomile.api.member.contoller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seomile.api.member.dto.MemberRequestDTO;
import seomile.api.member.entity.Member;
import seomile.api.member.service.MemberService;
import seomile.api.member.dto.MemberResponseDTO;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MemberController {
    private final MemberService memberService;

    @PostMapping("/signup")
    public ResponseEntity<Member> signup(
            @Valid @RequestBody MemberRequestDTO memberRequestDTO) throws Exception {
        return new ResponseEntity<>(memberService.signup(memberRequestDTO), HttpStatus.OK);
    }

    @PostMapping(value = "/login")
    public ResponseEntity<MemberResponseDTO> login(@RequestBody MemberRequestDTO request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @GetMapping("/user/get")
    public ResponseEntity<Member> getUser(@RequestHeader("Authorization") String authorizationHeader) {
        try {
            // "Bearer " 접두사 제거
            String token = authorizationHeader.startsWith("Bearer ")
                    ? authorizationHeader.substring(7)
                    : authorizationHeader;

            // MemberService에서 토큰을 통해 사용자 정보 가져옴
            Member member = memberService.getMemberFromToken(token);

            return new ResponseEntity<>(member, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED); // 토큰이 유효하지 않거나 오류가 발생한 경우
        }
    }

}