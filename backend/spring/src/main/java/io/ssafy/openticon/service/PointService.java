package io.ssafy.openticon.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


@RequiredArgsConstructor
@Service
public class PointService {
    // 트랜잭션 필요
    // TODO: 0. 결제 확인
    public void paymentCheck(){

    }

    // TODO: 1. 포인트 구매
    public void purchasePoints() {
        // 1. paymentCheck 로 결제를 확인
        // 2. point_history에 기록
        // 3. member의 point를 변경
        // 4. 완료
        // 포인트 구매 로직 (point_history에 기록)
    }

    // TODO: 2. 이모티콘 팩 구매
    public void purchaseEmoticonPack() {
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
        // 포인트 출금 로직 (point_history에 기록)
        // 1. 소지하고 있는 금액과 출금의 금액을 비교
        // 2. 소지하고 있는 금액이 많다면 point_history를 작성
        // 4. member의 point를 변경
        // 5. 완료
    }
}

