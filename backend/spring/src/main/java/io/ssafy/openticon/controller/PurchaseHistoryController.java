package io.ssafy.openticon.controller;

import io.ssafy.openticon.controller.response.PurchaseEmoticonResponseDto;
import io.ssafy.openticon.service.PurchaseHistoryService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/members/emoticnos")
public class PurchaseHistoryController {


    private final PurchaseHistoryService purchaseHistoryService;

    public PurchaseHistoryController(PurchaseHistoryService purchaseHistoryService) {
        this.purchaseHistoryService = purchaseHistoryService;
    }

    @GetMapping("")
    public ResponseEntity<List<PurchaseEmoticonResponseDto>> viewPurchasedEmoticons(@AuthenticationPrincipal UserDetails userDetails,
                                                                                    Pageable pageable){

        return ResponseEntity.status(HttpStatus.OK).body(purchaseHistoryService.viewPurchasedEmoticons(userDetails.getUsername(),pageable));
    }
}
