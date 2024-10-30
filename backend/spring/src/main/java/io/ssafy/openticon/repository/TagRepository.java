package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.PurchaseHistoryEntity;
import io.ssafy.openticon.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
    Optional<TagEntity> findByTagName(String tagName);
    long countByTagName(String tagName);
}
