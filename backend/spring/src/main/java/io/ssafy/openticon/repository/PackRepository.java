package io.ssafy.openticon.repository;

import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PackRepository extends JpaRepository<EmoticonPackEntity,Long> {
    public EmoticonPackEntity findByShareLink(String uuid);

    @Query("SELECT ep FROM EmoticonPackEntity ep WHERE ep.title LIKE %:title% AND ep.isBlacklist = false AND ep.isPublic = true")
    Page<EmoticonPackEntity> findByTitleContaining(String title, Pageable pageable);

    Page<EmoticonPackEntity> findAllByIsPublicTrueAndIsBlacklistFalse(Pageable pageable);

    @Query("SELECT ep FROM EmoticonPackEntity ep JOIN ep.tagLists tl JOIN tl.tag t WHERE t.tagName = :query AND ep.isBlacklist = false AND ep.isPublic = true")
    Page<EmoticonPackEntity> findByTag(@Param("query") String query, Pageable pageable);

    @Query("SELECT ep FROM EmoticonPackEntity ep JOIN ep.member m WHERE m.nickname = :query AND ep.isBlacklist = false AND ep.isPublic = true")
    Page<EmoticonPackEntity> findByAuthorContaining(@Param("query") String query, Pageable pageable);

    @Query("SELECT ep FROM EmoticonPackEntity ep WHERE ep.member = :member AND ep.isBlacklist = true")
    Page<EmoticonPackEntity> findByMemberAndIsBlacklist(@Param("member") MemberEntity member, Pageable pageable);

    @Query("SELECT ep FROM EmoticonPackEntity ep WHERE ep.member = :member")
    Page<EmoticonPackEntity> findByMyEmoticonPack(@Param("member") MemberEntity member, Pageable pageable);

}
