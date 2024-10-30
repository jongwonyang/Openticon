package io.ssafy.openticon.repository;

import io.ssafy.openticon.dto.TagListId;
import io.ssafy.openticon.entity.TagListEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagListRepository extends JpaRepository<TagListEntity, TagListId> {

}
