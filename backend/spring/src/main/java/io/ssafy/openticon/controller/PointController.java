package io.ssafy.openticon.controller;


import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import io.ssafy.openticon.controller.request.PointRequestDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/points")
public class PointController {
    @Autowired
    private IamportClient iamportClient;

    @PostMapping("/payment")
    public Map<String, String> paymentComplete(@RequestBody PointRequestDto request) {
        Map<String, String> response = new HashMap<>();
        // 유저가 건내준 imp_uid을 가져옴 ex)imp_749635900582
        String impUid = request.getImpUid();
        System.out.println(request.getMemberId()+"("+request.getImpUid()+"): "+request.getEmoticonPackId()+", "+request.getPoint());

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