package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.ObjectionManswerAnswerRequestDto;
import io.ssafy.openticon.controller.request.ObjectionSubmitRequestDto;
import io.ssafy.openticon.controller.request.ObjectionTestRequestDto;
import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.controller.response.*;
import io.ssafy.openticon.dto.ReportType;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.ObjectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;
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
import retrofit2.http.GET;

import java.util.NoSuchElementException;

@RestController
@RequestMapping("/objection")
@Tag(name = "이의제기")
public class ObjectionController {
    MemberRepository memberRepository;
    ObjectionService objectionService;

    public ObjectionController(MemberRepository memberRepository, ObjectionService objectionService){
        this.memberRepository = memberRepository;
        this.objectionService = objectionService;
    }

    @GetMapping("list")
    @Operation(summary = "심사를 통과하지 못하거나 누적 신고가 많은 이모티콘 팩 목록을 보여줍니다.")
    public ResponseEntity<Page<ObjectionListResponseDto>> listObjection(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.ok().body(objectionService.getObjectionList(member, pageable));
    }

    @PostMapping
    @Operation(summary = "사용자가 이모티콘 팩에 대한 이의제기를 신청합니다.")
    public ResponseEntity<ObjectionMsgResponseDto> submitObjection(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ObjectionSubmitRequestDto objectionRequest) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return ResponseEntity.ok().body(objectionService.submitObjection(member, objectionRequest));
    }

    @PostMapping("test")
    @Operation(summary = "<테스트 용> 이모티콘 팩 블랙리스트 만들고 이의제기 생성하기")
    public ResponseEntity<ObjectionMsgResponseDto> testObjection(@AuthenticationPrincipal UserDetails userDetails, @RequestBody ObjectionTestRequestDto request) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return ResponseEntity.ok().body(objectionService.testSubmitObjection(member, request));
    }

    @GetMapping("answer")
    @Operation(summary = "이의제기 심사 결과 확인하기")
    public ResponseEntity<AnswerResponseDto> answerObjection(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam Long objectionId) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return ResponseEntity.ok().body(objectionService.answerObjection(member, objectionId));
    }

    @GetMapping("manager-list")
    @Operation(summary = "관리자 이의 신청 목록")
    public ResponseEntity<Page<ObjectionListResponseDto>> managerListObjection(
            HttpServletRequest request,
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "id"));
        return ResponseEntity.ok().body(objectionService.managerObjection(member, pageable));
    }

    @PostMapping("manager-answer")
    @Operation(summary = "관리자의 이의제기 심사")
    public ResponseEntity<ObjectionMsgResponseDto> managerAnswerObjection(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody ObjectionManswerAnswerRequestDto requestDto
            ){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return ResponseEntity.ok().body(objectionService.managerAnswerObjection(member, requestDto));
    }
}
