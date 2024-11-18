package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.PurchaseHistoryController;
import io.ssafy.openticon.controller.response.PurchaseEmoticonResponseDto;
import io.ssafy.openticon.controller.response.PurchaseHistoryResponseDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import io.ssafy.openticon.repository.EmoticonRepository;
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
    private final EmoticonService emoticonService;

    public Optional<PurchaseHistoryResponseDto> isPurchaseHisotry(MemberEntity member, Long emoticonPackId){
        Optional<EmoticonPackEntity> emoticonPack = packRepository.findById(emoticonPackId);
        // 구매 기록이 있다면 true 없다면 false
        if (emoticonPack.isPresent()) {
            Optional<PurchaseHistoryEntity> purchaseHistory = purchaseHistoryRepository.findByMemberAndEmoticonPack(member, emoticonPack.get());
            PurchaseHistoryResponseDto purchaseHistoryResponseDto = PurchaseHistoryResponseDto.builder()
                    .isPurchase(purchaseHistory.isPresent())
                    .emoticonPackId(emoticonPack.get().getId())
                    .message(purchaseHistory.isPresent() ? "구매함" : "구매하지 않음")
                    .build();
            return Optional.of(purchaseHistoryResponseDto);
        }

        return Optional.empty();
    }

    public boolean isMemberPurchasePack(MemberEntity member, EmoticonPackEntity emoticonPack){
        if(purchaseHistoryRepository.findByMemberAndEmoticonPack(member,emoticonPack).isEmpty())return false;
        return true;
    }

    public List<PurchaseEmoticonResponseDto> viewPurchasedEmoticons(String email, boolean showAll, Pageable pageable){
        MemberEntity member=memberService.getMemberByEmail(email).orElseThrow();
        List<PurchaseEmoticonResponseDto> result = new ArrayList<>();

        if(showAll){
            Optional<List<PurchaseHistoryEntity>> purchaseHistoryEntities = purchaseHistoryRepository.findAllByMemberOrderByMemberAsc(member);
            if(purchaseHistoryEntities.isPresent()){
                for(PurchaseHistoryEntity purchaseHistoryEntity: purchaseHistoryEntities.get()){
                    EmoticonPackEntity emoticonPackEntity=purchaseHistoryEntity.getEmoticonPack();
                    PurchaseEmoticonResponseDto purchaseEmoticonResponseDto=PurchaseEmoticonResponseDto.builder()
                            .packId(emoticonPackEntity.getId())
                            .packName(emoticonPackEntity.getTitle())
                            .thumbnailImg(emoticonPackEntity.getThumbnailImg())
                            .listImage(emoticonPackEntity.getListImg())
                            .isPublic(emoticonPackEntity.isPublic())
                            .isHide(purchaseHistoryEntity.isHide())
                            .emoticons(emoticonService.getEmoticons(emoticonPackEntity.getId()))
                            .shareLink(emoticonPackEntity.getShareLink())
                            .build();
                    result.add(purchaseEmoticonResponseDto);
                }
            }
        }else{
            Page<PurchaseHistoryEntity> purchaseHistoryEntities=purchaseHistoryRepository.findAllByMember(member,pageable);
            for(PurchaseHistoryEntity purchaseHistoryEntity: purchaseHistoryEntities){
                EmoticonPackEntity emoticonPackEntity=purchaseHistoryEntity.getEmoticonPack();
                PurchaseEmoticonResponseDto purchaseEmoticonResponseDto=PurchaseEmoticonResponseDto.builder()
                        .packId(emoticonPackEntity.getId())
                        .packName(emoticonPackEntity.getTitle())
                        .thumbnailImg(emoticonPackEntity.getThumbnailImg())
                        .listImage(emoticonPackEntity.getListImg())
                        .isPublic(emoticonPackEntity.isPublic())
                        .isHide(purchaseHistoryEntity.isHide())
                        .emoticons(emoticonService.getEmoticons(emoticonPackEntity.getId()))
                        .build();

                result.add(purchaseEmoticonResponseDto);
            }
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
