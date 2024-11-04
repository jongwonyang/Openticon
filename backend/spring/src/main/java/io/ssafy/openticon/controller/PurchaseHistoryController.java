package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.SwitchViewRequestDto;
import io.ssafy.openticon.controller.response.PurchaseEmoticonResponseDto;
import io.ssafy.openticon.controller.response.PurchaseHistoryResponseDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.PackService;
import io.ssafy.openticon.service.PurchaseHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/purchased")
public class PurchaseHistoryController {


    private final PurchaseHistoryService purchaseHistoryService;
    private final PackService packService;
    private final MemberRepository memberRepository;

    public PurchaseHistoryController(PurchaseHistoryService purchaseHistoryService, PackService packService, MemberRepository memberRepository) {
        this.purchaseHistoryService = purchaseHistoryService;
        this.packService = packService;
        this.memberRepository = memberRepository;
    }

    @GetMapping("")
    @Operation(summary = "구매한 이모티콘팩을 보여줍니다.")
    public ResponseEntity<List<PurchaseEmoticonResponseDto>> viewPurchasedEmoticons(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "true") boolean all,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return ResponseEntity.status(HttpStatus.OK).body(purchaseHistoryService.viewPurchasedEmoticons(userDetails.getUsername(), all, pageable));
    }

    @GetMapping("history")
    @Operation(summary = "이모티콘 팩을 구매했는지 확인합니다.")
    public ResponseEntity<PurchaseHistoryResponseDto> isPurchaseEmoticon(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(required = true) Long emoticonPackId
    ){
        MemberEntity member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("사용자가 없습니다."));
        Optional<PurchaseHistoryResponseDto> purchaseHistoryResponseDto = purchaseHistoryService.isPurchaseHisotry(member, emoticonPackId);
        if(purchaseHistoryResponseDto.isEmpty()){
            PurchaseHistoryResponseDto emptyResponseDto = PurchaseHistoryResponseDto.builder()
                    .message("이모티콘 팩 입력이 잘못 되었습니다.")
                    .build();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(emptyResponseDto);
        }
        return ResponseEntity.ok().body(purchaseHistoryResponseDto.get());
    }

    @PostMapping("/view")
    @Operation(summary = "이모티콘을 숨기거나 보이게 합니다.")
    public ResponseEntity<Void> switchViewState(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SwitchViewRequestDto switchViewRequestDto){
        purchaseHistoryService.switchIsHide(userDetails.getUsername(),packService.getPackById(switchViewRequestDto.getPackId()));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
