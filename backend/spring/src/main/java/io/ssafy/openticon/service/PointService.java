package io.ssafy.openticon.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.controller.response.PointHistoryResponseDto;
import io.ssafy.openticon.controller.response.PurchasePointResponseDto;
import io.ssafy.openticon.dto.PointType;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PointHistoryEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import io.ssafy.openticon.repository.MemberRepository;
import io.ssafy.openticon.repository.PackRepository;
import io.ssafy.openticon.repository.PointHistoryRepository;
import io.ssafy.openticon.repository.PurchaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class PointService {
    @Autowired
    private IamportClient iamportClient;
    @Autowired
    private PointHistoryRepository pointHistoryRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private PackRepository packRepository;
    @Autowired
    private PurchaseHistoryRepository purchaseHistoryRepository;

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
    public ResponseEntity<PurchasePointResponseDto> purchasePoints(PointRequestDto pointRequestDto, MemberEntity member) {
        // 1. paymentCheck 로 결제를 확인

        // TODO: string인 경우에는 구매하도록 처리하였음. 나중에 지워야함
        if("success".equals(paymentCheck(pointRequestDto.getImpUid())) || pointRequestDto.getImpUid().equals("string")){
            // 2. point_history에 기록
            PointHistoryEntity pointHistory = PointHistoryEntity.builder()
                    .member(member)  // 멤버 정보
                    .type(PointType.DEPOSIT)      // 입금
                    .point(pointRequestDto.getPoint())    // 포인트 금액
                    .build();
            System.out.println(pointHistory.toString());
            pointHistoryRepository.save(pointHistory);  // 저장
            // 3. member의 point를 변경
            int point = member.getPoint() + pointRequestDto.getPoint();
            member.setPoint(point);
            memberRepository.save(member);
            // 4. 완료
            DecimalFormat formatter = new DecimalFormat("#,###");  // 숫자에 쉼표를 추가하는 형식
            String formattedPoint = formatter.format(pointRequestDto.getPoint());
            return ResponseEntity.ok().body(new PurchasePointResponseDto("success", formattedPoint+"원을 구매하였습니다."));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new PurchasePointResponseDto("error", "결제 정보가 잘못되었습니다."));
    }

    // TODO: 2. 이모티콘 팩 구매
    @Transactional
    public ResponseEntity<String> purchaseEmoticonPack(Long emoticonPackId, MemberEntity member) {
        // 이모티콘 팩이 있는지
        Optional<EmoticonPackEntity> emoticonPack = packRepository.findById(emoticonPackId);
        if(emoticonPack.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("이모티콘 팩을 찾을 수 없습니다.");
        }

        // 팩이 있다면 구매한 팩인지 확인
        Optional<PurchaseHistoryEntity> getPurchaseHistory = purchaseHistoryRepository.findByMemberAndEmoticonPack(member, emoticonPack.get());
        if(getPurchaseHistory.isPresent()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("이미 구매한 이모티콘 팩입니다.");
        }

        // 1. 소지하고 있는 금액과 이모티콘 팩의 금액을 비교
        if(member.getPoint() < emoticonPack.get().getPrice()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("소지 금액이 부족합니다.");
        }

        // 2. 소지하고 있는 금액이 많다면 purchase_history를 작성
        PurchaseHistoryEntity purchaseHistory = PurchaseHistoryEntity.builder()
                .member(member)
                .emoticonPack(emoticonPack.get())
                .build();
        purchaseHistoryRepository.save(purchaseHistory);

        // 3. point_history를 작성
        // 구매자
        PointHistoryEntity purchasePointHistory = PointHistoryEntity.builder()
                .member(member)  // 멤버 정보
                .type(PointType.PURCHASE)      // 구입
                .point(emoticonPack.get().getPrice())    // 포인트 금액
                .build();
        pointHistoryRepository.save(purchasePointHistory);

        // 판매자
        PointHistoryEntity salePointHistory = PointHistoryEntity.builder()
                .member(emoticonPack.get().getMember())  // 멤버 정보
                .type(PointType.SALE)      // 판매
                .point(emoticonPack.get().getPrice())    // 포인트 금액
                .build();
        pointHistoryRepository.save(salePointHistory);

        // 4. member의 point를 변경
        int point = member.getPoint() - emoticonPack.get().getPrice();
        member.setPoint(point);
        memberRepository.save(member);

        // 5. 완료
        return ResponseEntity.ok().body(emoticonPack.get().getTitle()+" 이모티콘 팩을 성공적으로 구매하였습니다.");
    }

    // TODO: 3. 포인트 기록 보기
    public Page<PointHistoryResponseDto> getPointHistory(MemberEntity member, Pageable pageable) {
        // 포인트 기록 조회 로직
        Page<PointHistoryEntity> pointHistoryPage = pointHistoryRepository.findAllByMember(member, pageable);

        // 엔티티를 DTO로 변환
        List<PointHistoryResponseDto> pointHistoryDtos = pointHistoryPage.getContent().stream()
                .map(history -> new PointHistoryResponseDto(
                        history.getId(),
                        history.getType(),
                        history.getPoint(),
                        history.getCreatedAt()
                ))
                .toList();

        return new PageImpl<>(pointHistoryDtos, pageable, pointHistoryPage.getTotalElements());
    }

    // TODO: 4. 포인트 출금
    public ResponseEntity<String> withdrawPoints(MemberEntity member, int withdrawPoint) {
        // 1. 소지하고 있는 금액과 출금 금액을 비교
        if(member.getPoint() < withdrawPoint){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("소지 금액이 부족합니다.");
        }

        // 2. point_history를 작성
        PointHistoryEntity pointHistory = PointHistoryEntity.builder()
                .member(member)  // 멤버 정보
                .type(PointType.WITHDRAW)      // 출금
                .point(withdrawPoint)    // 포인트 금액
                .build();
        pointHistoryRepository.save(pointHistory);

        // 3. member의 point를 변경
        int point = member.getPoint() - withdrawPoint;
        member.setPoint(point);
        memberRepository.save(member);

        DecimalFormat formatter = new DecimalFormat("#,###");  // 숫자에 쉼표를 추가하는 형식
        String formattedPoint = formatter.format(withdrawPoint);

        return ResponseEntity.ok().body(formattedPoint + "원을 출금하였습니다.");
    }
}

