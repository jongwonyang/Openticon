package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.EditDeviceTokenRequestDto;
import io.ssafy.openticon.controller.response.MemberResponseDto;
import io.ssafy.openticon.repository.MemberRepository;
//import io.ssafy.openticon.service.ImageService;
import io.ssafy.openticon.service.MemberService;
import io.ssafy.openticon.service.PackService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.io.IOException;

@RestController
@RequestMapping("member")
@AllArgsConstructor
@Tag(name = "Member API")
public class MemberController {

    private final MemberRepository memberRepository;
    private final PackService packService;
    private final MemberService memberService;
//    private final ImageService imageService;

    @PutMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(description = "개인정보 수정")
    public ResponseEntity<String> editMember(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "프로필 이미지", required = false)
            @RequestPart(value = "profile_img", required = false) MultipartFile profileImg,
            @Parameter(description = "닉네임", required = true)
            @RequestParam("nickname") String nickname,
            @Parameter(description = "상태메시지", required = true)
            @RequestParam("bio") String bio)
    {
        Logger logger = LoggerFactory.getLogger(this.getClass());

        try {
            // 사용자를 이메일로 조회
            MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));

            // 닉네임 설정
            member.setNickname(nickname);
            member.setBio(bio);

            // 프로필 이미지가 있을 경우에만 저장
            if (profileImg != null && !profileImg.isEmpty()) {

                logger.info("프로필 이미지 업로드: 파일명 = {}, 크기 = {} bytes", profileImg.getOriginalFilename(), profileImg.getSize());

                // 이미지 저장 로직 호출
//                String profileImageUrl = memberService.saveProfile(imageService.convertTransparentToWhiteBackground(profileImg));
                String profileImageUrl = memberService.saveProfile(profileImg, false);
                member.setProfile_image(profileImageUrl);

                logger.info("프로필 이미지 저장 완료: URL = {}", profileImageUrl);
            } else {
                logger.info("프로필 이미지가 제공되지 않았거나 비어 있습니다.");
            }
            // 변경된 회원 정보 저장
            memberRepository.save(member);
            logger.info("회원 정보 수정 완료: 닉네임 = {}", nickname);
            return ResponseEntity.ok("회원 정보가 성공적으로 수정되었습니다.");
        } catch (ResponseStatusException e) {
            logger.error("사용자 조회 실패: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("사용자가 존재하지 않습니다.");
        } catch (Exception e) {
            logger.error("회원 정보 수정 중 오류 발생: {}", e.getMessage());
            return ResponseEntity.internalServerError().body("오류 발생: " + e.getMessage());
        }
    }

    @DeleteMapping("")
    @Operation(description = "회원탈퇴")
    public  ResponseEntity<String> deleteMember(@AuthenticationPrincipal UserDetails userDetails){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        member.setIsResigned(true);
        memberRepository.save(member);
        return ResponseEntity.ok().body("회원 탈퇴 성공");
    }

    @GetMapping()
    public ResponseEntity<MemberResponseDto> getMember(@AuthenticationPrincipal UserDetails userDetails) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return ResponseEntity.ok(new MemberResponseDto(member));
    }
    @GetMapping("duplicate-check")
    public ResponseEntity<String> checkNickNameDupl(
            @AuthenticationPrincipal UserDetails userDetails,
            @Parameter(description = "닉네임", required = true)
            @RequestParam("nickname") String nickname) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        if(Objects.equals(nickname, member.getNickname())){
            return ResponseEntity.ok("사용 가능한 닉네임입니다.");
        }
        boolean isDuplicate = memberRepository.existsMemberByNickname(nickname);
        if (isDuplicate) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("닉네임이 이미 존재합니다.");
        } else {
            return ResponseEntity.ok("사용 가능한 닉네임입니다.");
        }
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

    @GetMapping("/writer")
    public ResponseEntity<MemberEntity> getWriter(@RequestParam String nickname) {
        return memberRepository.findByNickname(nickname)
                .map(member -> ResponseEntity.ok(member))
                .orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }
}
