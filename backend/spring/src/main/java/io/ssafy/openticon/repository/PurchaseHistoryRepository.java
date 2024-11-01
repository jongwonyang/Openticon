package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryEntity, Long> {
    Optional<PurchaseHistoryEntity> findByMemberAndEmoticonPack(MemberEntity member, EmoticonPackEntity emoticonPack);

    List<PurchaseHistoryEntity> findByMember(MemberEntity member);

    Page<PurchaseHistoryEntity> findAllByMember(MemberEntity member, Pageable pageable);

}
