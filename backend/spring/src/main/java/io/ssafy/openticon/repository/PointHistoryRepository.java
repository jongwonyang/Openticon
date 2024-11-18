package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PointHistoryEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PointHistoryRepository extends JpaRepository<PointHistoryEntity, Long> {
    Page<PointHistoryEntity> findAllByMember(MemberEntity member, Pageable pageable);
}
