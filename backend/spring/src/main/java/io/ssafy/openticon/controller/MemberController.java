package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EditDeviceTokenRequestDto;
import io.ssafy.openticon.repository.MemberRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import io.ssafy.openticon.entity.Member;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@RestController
@RequestMapping("member")
@AllArgsConstructor
@Tag(name = "Member API")
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping
    @Operation(summary = "내 정보 보기")
    public ResponseEntity<Member> getMember(@AuthenticationPrincipal UserDetails userDetails) {
        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));

        return ResponseEntity.ok(member);
    }

    @PostMapping("deviceToken")
    @Operation(summary = "device token 저장 api")
    public ResponseEntity<String> editDeviceToken(@AuthenticationPrincipal UserDetails userDetails,@RequestBody EditDeviceTokenRequestDto editDeviceTokenRequestDto) {

        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));

        if (editDeviceTokenRequestDto.getDeviceToken() == null) {
            return ResponseEntity.badRequest().body("디바이스 토큰이 없습니다.");
        }

        if (editDeviceTokenRequestDto.isMobile()) {
            member.setMobile_fcm(editDeviceTokenRequestDto.getDeviceToken());
        } else {
            member.setWeb_fcm(editDeviceTokenRequestDto.getDeviceToken());
        }

        memberRepository.save(member);
        return ResponseEntity.ok().body("디바이스 토큰 저장 성공!");
    }
}
