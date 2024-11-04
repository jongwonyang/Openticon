package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
import io.ssafy.openticon.controller.response.PurchasePointResponseDto;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.ObjectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping("/objection")
@Tag(name = "이의제기")
public class ObjectionController {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    ObjectionService objectionService;


    @GetMapping("list")
    @Operation(summary = "심사를 통과하지 못하거나 누적 신고가 많은 이모티콘 팩 목록을 보여줍니다.")
    public ResponseEntity<Page<EmoticonPackResponseDto>> listObjection(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok().body(objectionService.getObjectionList(member, pageable));
    }

    @PostMapping("submit")
    @Operation(summary = "사용자가 이모티콘 팩에 대한 이의제기를 신청합니다.")
    public ResponseEntity<?> submitObjection(@AuthenticationPrincipal UserDetails userDetails) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return ResponseEntity.ok().build();
    }
}
