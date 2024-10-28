package io.ssafy.openticon.controller;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.entity.Member;
import io.ssafy.openticon.repository.MemberRepository;
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
    private IamportClient iamportClient;

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/payment")
    @Operation(summary = "사용자의 결제에 대한 검증을 합니다.")
    public Map<String, String> paymentComplete(@RequestBody PointRequestDto request, @AuthenticationPrincipal UserDetails userDetails) {
        // 유저 정보 가지고옴
        Member member = memberRepository.findMemberByEmail(userDetails.getUsername())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "사용자가 없습니다."));

        Map<String, String> response = new HashMap<>();
        // 유저가 건내준 imp_uid을 가져옴 ex)imp_749635900582
        String impUid = request.getImpUid();
        System.out.println(member.getId()+"("+request.getImpUid()+"): "+request.getEmoticonPackId()+", "+request.getPoint());

        try {
            // iamport 서버에서 유저의 imp_uid 값을 확인해서 결제 내역이 있는지 확인 >> 결제가 성공이라면 paid
            Payment payment = iamportClient.paymentByImpUid(impUid).getResponse();

            // 아래는 결제 내역이 있는 경우
            if ("paid".equals(payment.getStatus())) {
                System.out.println(payment.getStatus());
                response.put("status", "success");
            } else {
                System.out.println(payment.getStatus());
                response.put("status", "fail");
            }
        } catch (Exception e) { // 결제 내역이 없는 경우
            System.out.println("error");
            response.put("status", "error");
        }
        return response;
    }
}