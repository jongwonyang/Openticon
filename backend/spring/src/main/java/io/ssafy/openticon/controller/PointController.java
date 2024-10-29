package io.ssafy.openticon.controller;


import io.ssafy.openticon.controller.request.EmoticonPackPurchaseRequestDto;
import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.controller.request.PointWithdrawRequestDto;
import io.ssafy.openticon.controller.response.PointHistoryResponseDto;
import io.ssafy.openticon.controller.response.PurchasePointResponseDto;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.PointService;
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
@RequestMapping("/points")
@Tag(name = "포인트")
public class PointController {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointService pointService;

    @PostMapping("/purchase-point")
    @Operation(summary = "사용자가 포인트를 구매합니다.")
    public ResponseEntity<PurchasePointResponseDto> purchasePoint(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return pointService.purchasePoints(request, member);
    }

    @PostMapping("/purchase-pack")
    @Operation(summary = "사용자가 이모티콘 팩을 구매합니다.")
    public ResponseEntity<String> purchasePack(@RequestBody EmoticonPackPurchaseRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return pointService.purchaseEmoticonPack(request.getEmoticonPackId(), member);
    }

    @GetMapping("/list")
    @Operation(summary = "자신의 포인트 리스트를 조회합니다.")
    public ResponseEntity<?> getPointsList(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<PointHistoryResponseDto> pointHistoryPage = pointService.getPointHistory(member, pageable);
        if (pointHistoryPage.hasContent()) {
            return ResponseEntity.ok(pointHistoryPage);
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body("포인트 기록이 없습니다.");
    }

    @PostMapping("/withdraw-point")
    @Operation(summary = "포인트를 출금합니다.")
    public ResponseEntity<String> withdrawPoint(@RequestBody PointWithdrawRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return pointService.withdrawPoints(member, request.getPoint());
    }
}