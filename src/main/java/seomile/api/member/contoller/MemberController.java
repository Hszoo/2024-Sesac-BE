package seomile.api.member.contoller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import seomile.api.member.dto.MemberRequestDTO;
import seomile.api.member.repository.MemberRepository;
import seomile.api.member.service.MemberService;
import seomile.api.member.dto.MemberResponseDTO;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody MemberRequestDTO request) throws Exception {
        return new ResponseEntity<>(memberService.login(request), HttpStatus.OK);
    }

    @PostMapping(value = "/signup")
    public ResponseEntity<String> signup(@RequestBody MemberRequestDTO request) throws Exception {
        log.info("request = {}", request);
        return new ResponseEntity<>(memberService.signup(request), HttpStatus.OK);
    }

    @GetMapping("/get")
    public ResponseEntity<List<MemberResponseDTO>> getAllUsers() {
        List<MemberResponseDTO> members = memberService.getAllMembers();
        return new ResponseEntity<>(members, HttpStatus.OK);
    }

    @GetMapping("/admin/get")
    public ResponseEntity<MemberResponseDTO> getUserForAdmin(@RequestParam String account) throws Exception {
        return new ResponseEntity<>( memberService.getMember(account), HttpStatus.OK);
    }

}