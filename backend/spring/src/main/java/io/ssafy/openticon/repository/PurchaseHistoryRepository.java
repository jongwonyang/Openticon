package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryEntity, Long> {
    Optional<PurchaseHistoryEntity> findByMemberAndEmoticonPack(MemberEntity member, EmoticonPackEntity emoticonPack);

    List<PurchaseHistoryEntity> findByMember(MemberEntity member);

    Page<PurchaseHistoryEntity> findAllByMember(MemberEntity member, Pageable pageable);

    Optional<List<PurchaseHistoryEntity>> findAllByMemberOrderByMemberAsc(MemberEntity member);

    // 각 EmoticonPackEntity의 다운로드 수를 조회
    @Query("SELECT p.emoticonPack.id, COUNT(p) FROM PurchaseHistoryEntity p GROUP BY p.emoticonPack.id")
    List<Object[]> findDownloadCountsByEmoticonPack();
}
