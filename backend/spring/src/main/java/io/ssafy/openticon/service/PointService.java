package io.ssafy.openticon.service;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.response.Payment;
import io.ssafy.openticon.controller.request.PointRequestDto;
import io.ssafy.openticon.controller.response.PointHistoryResponseDto;
import io.ssafy.openticon.controller.response.PointResponseDto;
import io.ssafy.openticon.dto.PointType;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PointHistoryEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
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

import java.awt.*;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PointService {
    private final IamportClient iamportClient;
    private final PointHistoryRepository pointHistoryRepository;
    private final MemberRepository memberRepository;
    private final PackRepository packRepository;
    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public PointService(IamportClient iamportClient,
                        PointHistoryRepository pointHistoryRepository,
                        MemberRepository memberRepository,
                        PackRepository packRepository,
                        PurchaseHistoryRepository purchaseHistoryRepository){
        this.iamportClient = iamportClient;
        this.pointHistoryRepository = pointHistoryRepository;
        this.memberRepository = memberRepository;
        this.packRepository = packRepository;
        this.purchaseHistoryRepository = purchaseHistoryRepository;
    }

    public String paymentCheck(String impUid){
        try {
            Payment payment = iamportClient.paymentByImpUid(impUid).getResponse();

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

    @Transactional
    public PointResponseDto purchasePoints(PointRequestDto pointRequestDto, MemberEntity member) {
        if(pointRequestDto.getPoint() <= 0){
            throw new OpenticonException(ErrorCode.NON_POSITIVE_INTEGER);
        }
        if("success".equals(paymentCheck(pointRequestDto.getImpUid())) || pointRequestDto.getImpUid().equals("string")){

            PointHistoryEntity pointHistory = PointHistoryEntity.builder()
                    .member(member)  // 멤버 정보
                    .type(PointType.DEPOSIT)      // 입금
                    .point(pointRequestDto.getPoint())    // 포인트 금액
                    .build();
            System.out.println(pointHistory.toString());
            pointHistoryRepository.save(pointHistory);  // 저장

            int point = member.getPoint() + pointRequestDto.getPoint();
            member.setPoint(point);
            memberRepository.save(member);

            DecimalFormat formatter = new DecimalFormat("#,###");  // 숫자에 쉼표를 추가하는 형식
            String formattedPoint = formatter.format(pointRequestDto.getPoint());
            return new PointResponseDto("OK", formattedPoint+"원을 구매하였습니다.");
        }
        throw new OpenticonException(ErrorCode.IAM_PORT_PAYMENT_ERROR);
    }

    @Transactional
    public PointResponseDto purchaseEmoticonPack(Long emoticonPackId, MemberEntity member) {
        EmoticonPackEntity emoticonPack = packRepository.findById(emoticonPackId)
                .orElseThrow(() -> new OpenticonException(ErrorCode.EMOTICON_PACK_EMPTY));

        Optional<PurchaseHistoryEntity> getPurchaseHistory = purchaseHistoryRepository.findByMemberAndEmoticonPack(member, emoticonPack);
        if(getPurchaseHistory.isPresent()){
            throw new OpenticonException(ErrorCode.DUPLICATE_EMOTICON_PACK_PURCHASE);
        }

        if(member.getPoint() < emoticonPack.getPrice()){
            throw new OpenticonException(ErrorCode.INSUFFICIENT_BALANCE_ERROR);
        }

        // 구매기록 추가
        PurchaseHistoryEntity purchaseHistory = PurchaseHistoryEntity.builder()
                .member(member)
                .emoticonPack(emoticonPack)
                .build();
        purchaseHistoryRepository.save(purchaseHistory);

        // 구매자
        PointHistoryEntity purchasePointHistory = PointHistoryEntity.builder()
                .member(member)  // 멤버 정보
                .type(PointType.PURCHASE)      // 구입
                .point(emoticonPack.getPrice())    // 포인트 금액
                .build();
        pointHistoryRepository.save(purchasePointHistory);

        // 판매자
        PointHistoryEntity salePointHistory = PointHistoryEntity.builder()
                .member(emoticonPack.getMember())  // 멤버 정보
                .type(PointType.SALE)      // 판매
                .point(emoticonPack.getPrice())    // 포인트 금액
                .build();
        pointHistoryRepository.save(salePointHistory);

        // 소지 금액 수정
        int point = member.getPoint() - emoticonPack.getPrice();
        member.setPoint(point);
        emoticonPack.setDownload(emoticonPack.getDownload() + 1);
        memberRepository.save(member);

        return new PointResponseDto("OK", emoticonPack.getTitle()+" 이모티콘 팩을 성공적으로 구매하였습니다.");
    }

    public Page<PointHistoryResponseDto> getPointHistory(MemberEntity member, Pageable pageable) {
        Page<PointHistoryEntity> pointHistoryPage = pointHistoryRepository.findAllByMember(member, pageable);

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

    public PointResponseDto withdrawPoints(MemberEntity member, int withdrawPoint) {
        if(withdrawPoint <= 0){
            throw new OpenticonException(ErrorCode.NON_POSITIVE_INTEGER);
        }

        if(member.getPoint() < withdrawPoint){
            throw new OpenticonException(ErrorCode.INSUFFICIENT_BALANCE_ERROR);
        }

        PointHistoryEntity pointHistory = PointHistoryEntity.builder()
                .member(member)  // 멤버 정보
                .type(PointType.WITHDRAW)      // 출금
                .point(withdrawPoint)    // 포인트 금액
                .build();
        pointHistoryRepository.save(pointHistory);

        int point = member.getPoint() - withdrawPoint;
        member.setPoint(point);
        memberRepository.save(member);

        DecimalFormat formatter = new DecimalFormat("#,###");  // 숫자에 쉼표를 추가하는 형식
        String formattedPoint = formatter.format(withdrawPoint);
        return new PointResponseDto("OK", formattedPoint + "원을 출금하였습니다.");
    }
}

