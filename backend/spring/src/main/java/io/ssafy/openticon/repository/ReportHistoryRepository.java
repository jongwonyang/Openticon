package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.ReportHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReportHistoryRepository extends JpaRepository<ReportHistoryEntity,Long> {
    Optional<ReportHistoryEntity> findByMemberIdAndEmoticonPackIdAndDeletedAtIsNull(Long memberId, Long emoticonPackId);

    List<ReportHistoryEntity> findByEmoticonPackIdAndDeletedAtIsNull(Long emoticonPackId);
}
