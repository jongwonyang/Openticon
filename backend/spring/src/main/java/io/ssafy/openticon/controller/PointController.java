package io.ssafy.openticon.controller;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.entity.Member;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.service.PointService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/points")
@Tag(name = "포인트")
public class PointController {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PointService pointService;

    @PostMapping("/payment")
    @Operation(summary = "사용자의 결제에 대한 검증을 합니다.")
    public String paymentComplete(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        // 유저 정보 가지고옴
        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        return pointService.paymentCheck(request.getImpUid()); // success, fail, error
    }

    @PostMapping("/purchase-point")
    @Operation(summary = "사용자가 포인트를 구매합니다.")
    public String purchasePoint(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        // 유저 정보 가지고옴
        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));
        if(pointService.purchasePoints(request, member)){
            return "success";
        }
        return "fail";
    }

    @PostMapping("/purchase-pack")
    @Operation(summary = "사용자가 이모티콘 팩을 구매합니다.")
    public String purchasePack(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {

        return "success";
    }

    @GetMapping("/list")
    @Operation(summary = "사용자가 이모티콘 팩을 구매합니다.")
    public String getPointsList(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {

        return "success";
    }

    @PostMapping("/withdraw-point")
    @Operation(summary = "사용자가 이모티콘 팩을 구매합니다.")
    public String withdrawPoint(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {

        return "success";
    }


}