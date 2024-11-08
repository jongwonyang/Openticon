package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.DownloadEntity;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadRepository extends JpaRepository<DownloadEntity, Long> {
    DownloadEntity findByEmoticonPackId(Long emoticonPackId);
}
