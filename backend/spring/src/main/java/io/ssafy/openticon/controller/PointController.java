package io.ssafy.openticon.controller;


import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.controller.response.PurchasePointReponseDto;
import io.ssafy.openticon.entity.Member;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.parameters.P;
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
    public ResponseEntity<PurchasePointReponseDto> purchasePoint(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        PurchasePointReponseDto purchasePointReponseDto = null;
        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        if(pointService.purchasePoints(request, member)){
            purchasePointReponseDto = new PurchasePointReponseDto("success", request.getPoint()+"원 구매 성공");
            return ResponseEntity.ok(purchasePointReponseDto);
        }
        purchasePointReponseDto = new PurchasePointReponseDto("error", "포인트 구매에 실패하였습니다.");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(purchasePointReponseDto);
    }

    @PostMapping("/purchase-pack")
    @Operation(summary = "사용자가 이모티콘 팩을 구매합니다.")
    public ResponseEntity<String> purchasePack(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok("성공");
    }

    @GetMapping("/list")
    @Operation(summary = "사용자가 이모티콘 팩을 구매합니다.")
    public ResponseEntity<String> getPointsList(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok("성공");
    }

    @PostMapping("/withdraw-point")
    @Operation(summary = "사용자가 이모티콘 팩을 구매합니다.")
    public ResponseEntity<String> withdrawPoint(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {

        return ResponseEntity.ok("성공");
    }


}