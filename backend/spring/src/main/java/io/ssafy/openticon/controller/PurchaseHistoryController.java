package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.request.SwitchViewRequestDto;
import io.ssafy.openticon.controller.response.PurchaseEmoticonResponseDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.service.PackService;
import io.ssafy.openticon.service.PurchaseHistoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/purchased")
public class PurchaseHistoryController {


    private final PurchaseHistoryService purchaseHistoryService;
    private final PackService packService;

    public PurchaseHistoryController(PurchaseHistoryService purchaseHistoryService, PackService packService) {
        this.purchaseHistoryService = purchaseHistoryService;
        this.packService = packService;
    }

    @GetMapping("")
    @Operation(summary = "구매한 이모티콘팩을 보여줍니다.")
    public ResponseEntity<List<PurchaseEmoticonResponseDto>> viewPurchasedEmoticons(@AuthenticationPrincipal UserDetails userDetails,
                                                                                    Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(purchaseHistoryService.viewPurchasedEmoticons(userDetails.getUsername(),pageable));
    }

    @PostMapping("/view")
    @Operation(summary = "이모티콘을 숨기거나 보이게 합니다.")
    public ResponseEntity<Void> switchViewState(@AuthenticationPrincipal UserDetails userDetails, @RequestBody SwitchViewRequestDto switchViewRequestDto){
        purchaseHistoryService.switchIsHide(userDetails.getUsername(),packService.getPackById(switchViewRequestDto.getPackId()));

        return ResponseEntity.status(HttpStatus.OK).build();
    }
}
