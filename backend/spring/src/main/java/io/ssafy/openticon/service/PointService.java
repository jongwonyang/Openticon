package io.ssafy.openticon.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.dto.PointType;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PointHistoryEntity;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.repository.PointHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@RequiredArgsConstructor
@Service
public class PointService {
    @Autowired
    private IamportClient iamportClient;
    @Autowired
    private PointHistoryRepository pointHistoryRepository;
    @Autowired
    private MemberRepository memberRepository;

    // 트랜잭션 필요
    // TODO: 0. 결제 확인
    public String paymentCheck(String impUid){
        try {
            // iamport 서버에서 유저의 imp_uid 값을 확인해서 결제 내역이 있는지 확인 >> 결제가 성공이라면 paid
            Payment payment = iamportClient.paymentByImpUid(impUid).getResponse();

            // 아래는 결제 내역이 있는 경우
            if ("paid".equals(payment.getStatus())) {
                System.out.println(payment.getStatus());
                return "success";
            } else {
                System.out.println(payment.getStatus());
                return "fail";
            }
        } catch (Exception e) { // 결제 내역이 없는 경우
            return "error";
        }
    }

    // TODO: 1. 포인트 구매
    @Transactional
    public boolean purchasePoints(PointRequestDto pointRequestDto, MemberEntity member) {
        // 1. paymentCheck 로 결제를 확인
        if("success".equals(paymentCheck(pointRequestDto.getImpUid()))){
            // 2. point_history에 기록
            PointHistoryEntity pointHistory = PointHistoryEntity.builder()
                    .member(member)  // 멤버 정보
                    .type(PointType.PURCHASE)      // 포인트 타입 설정
                    .point(pointRequestDto.getPoint())    // 포인트 금액
                    .build();
            System.out.println(pointHistory.toString());
            pointHistoryRepository.save(pointHistory);  // 저장
            // 3. member의 point를 변경
            Long point = member.getPoint() + pointRequestDto.getPoint();
            member.setPoint(point);
            memberRepository.save(member);
            // 4. 완료
            return true;
        }
        return false;
    }

    // TODO: 2. 이모티콘 팩 구매
    public void purchaseEmoticonPack(MemberEntity member) {
        // 1. 소지하고 있는 금액과 이모티콘 팩의 금액을 비교
        // 2. 소지하고 있는 금액이 많다면 purchase_history를 작성
        // 3. point_history를 작성
        // 4. member의 point를 변경
        // 5. 완료
        // 이모티콘 팩 구매 로직 (purchase_history 및 point_history에 기록)
    }

    // TODO: 3. 포인트 기록 보기
//    public List<PointHistoryDto> getPointHistory() {
//        // 포인트 기록 조회 로직
//        return new ArrayList<>();
//    }

    // TODO: 4. 포인트 출금
    public void withdrawPoints() {
        // 1. 소지하고 있는 금액과 출금의 금액을 비교
        // 2. 소지하고 있는 금액이 많다면 point_history를 작성
        // 4. member의 point를 변경
        // 5. 완료
    }
}

