package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryEntity, Long> {
    Optional<PurchaseHistoryEntity> findByMemberAndEmoticonPack(MemberEntity member, EmoticonPackEntity emoticonPack);
}
