package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EditDeviceTokenRequestDto;
import io.ssafy.openticon.controller.response.MemberResponseDto;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.MemberService;
import io.ssafy.openticon.service.PackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import io.ssafy.openticon.entity.MemberEntity;

@RestController
@RequestMapping("member")
@AllArgsConstructor
@Tag(name = "Member API")
public class MemberController {

    private final MemberRepository memberRepository;
    private final PackService packService;
    private final MemberService memberService;

    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "개인정보 수정")
    public ResponseEntity<String> editMember(@AuthenticationPrincipal UserDetails userDetails,
                                             @Parameter(description = "프로필 이미지", required = false)
                                             @RequestPart("profile_img") MultipartFile profile_img,
                                             @Parameter(description = "닉네임", required = true) @RequestPart("nickname") String nickname){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        member.setNickname(nickname);
        try{
            if (profile_img != null) {
                member.setProfile_image(memberService.saveProfile(profile_img));
            }
            memberRepository.save(member);
        }catch (Exception e){
            return ResponseEntity.internalServerError().body("이미지 저장 실패");
        }

        return ResponseEntity.ok("회원 정보가 수정되었습니다.");
    }


    @GetMapping()
    public ResponseEntity<MemberResponseDto> getMember(@AuthenticationPrincipal UserDetails userDetails) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        return ResponseEntity.ok(new MemberResponseDto(member));
    }

    @PostMapping("deviceToken")
    @Operation(summary = "device token 저장 api")
    public ResponseEntity<String> editDeviceToken(@AuthenticationPrincipal UserDetails userDetails,@RequestBody EditDeviceTokenRequestDto editDeviceTokenRequestDto) {

        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
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
