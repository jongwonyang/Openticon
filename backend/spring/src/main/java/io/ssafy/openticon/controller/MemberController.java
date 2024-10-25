package io.ssafy.openticon.controller;

import io.ssafy.openticon.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.ssafy.openticon.entity.Member;

@RestController
@AllArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;


    @GetMapping("member")
    public ResponseEntity<Member> getMember(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        return ResponseEntity.ok(member);
    }

}
