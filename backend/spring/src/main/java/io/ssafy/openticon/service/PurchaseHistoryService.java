package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.PurchaseHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PurchaseHistoryService {

    private final PurchaseHistoryRepository purchaseHistoryRepository;

    public boolean isMemberPurchasePack(MemberEntity member, EmoticonPackEntity emoticonPack){
        if(purchaseHistoryRepository.findByMemberAndEmoticonPack(member,emoticonPack).isEmpty())return false;
        return true;
    }
}
