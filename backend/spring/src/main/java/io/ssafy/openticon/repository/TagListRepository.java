package io.ssafy.openticon.repository;

import io.ssafy.openticon.dto.TagListId;
import io.ssafy.openticon.entity.TagListEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TagListRepository extends JpaRepository<TagListEntity, TagListId> {
    // tagName으로 TagList 조회
    @Query("SELECT t FROM TagListEntity t JOIN t.tag tag WHERE tag.tagName = :tagName")
    List<TagListEntity> findTagListsByTagName(@Param("tagName") String tagName);
}
