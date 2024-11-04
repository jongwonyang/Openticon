package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.response.PurchaseEmoticonResponseDto;
import io.ssafy.openticon.controller.response.PurchaseHistoryResponseDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import io.ssafy.openticon.repository.PackRepository;
import io.ssafy.openticon.repository.PurchaseHistoryRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository purchaseHistoryRepository;
    private final MemberService memberService;
    private final PackRepository packRepository;

    public Optional<PurchaseHistoryResponseDto> isPurchaseHisotry(MemberEntity member, Long emoticonPackId){
        Optional<EmoticonPackEntity> emoticonPack = packRepository.findById(emoticonPackId);
        // 구매 기록이 있다면 true 없다면 false
        if (emoticonPack.isPresent()) {
            Optional<PurchaseHistoryEntity> purchaseHistory = purchaseHistoryRepository.findByMemberAndEmoticonPack(member, emoticonPack.get());
            PurchaseHistoryResponseDto purchaseHistoryResponseDto = PurchaseHistoryResponseDto.builder()
                    .isPurchase(purchaseHistory.isPresent())
                    .emoticonPackId(emoticonPack.get().getId())
                    .build();
            return Optional.of(purchaseHistoryResponseDto);
        }

        return Optional.empty();
    }

    public boolean isMemberPurchasePack(MemberEntity member, EmoticonPackEntity emoticonPack){
        if(purchaseHistoryRepository.findByMemberAndEmoticonPack(member,emoticonPack).isEmpty())return false;
        return true;
    }

    public List<PurchaseEmoticonResponseDto> viewPurchasedEmoticons(String email, Pageable pageable){
        MemberEntity member=memberService.getMemberByEmail(email).orElseThrow();

        Page<PurchaseHistoryEntity> purchaseHistoryEntities=purchaseHistoryRepository.findAllByMember(member,pageable);

        List<PurchaseEmoticonResponseDto> result=new ArrayList<>();
        for(PurchaseHistoryEntity purchaseHistoryEntity: purchaseHistoryEntities){
            EmoticonPackEntity emoticonPackEntity=purchaseHistoryEntity.getEmoticonPack();
            PurchaseEmoticonResponseDto purchaseEmoticonResponseDto=PurchaseEmoticonResponseDto.builder()
                    .packName(emoticonPackEntity.getTitle())
                    .thumbnailImg(emoticonPackEntity.getThumbnailImg())
                    .isHide(purchaseHistoryEntity.isHide())
                    .build();

            result.add(purchaseEmoticonResponseDto);
        }

        return result;
    }

    public void switchIsHide(String email, EmoticonPackEntity packEntity){
        MemberEntity member = memberService.getMemberByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("멤버를 찾을 수 없음"));

        PurchaseHistoryEntity purchaseHistoryEntity = purchaseHistoryRepository
                .findByMemberAndEmoticonPack(member, packEntity)
                .orElseThrow(() -> new EntityNotFoundException("구매기록을 찾을 수 없음"));

        purchaseHistoryEntity.setHide(!purchaseHistoryEntity.isHide());

        purchaseHistoryRepository.save(purchaseHistoryEntity);
    }
}
