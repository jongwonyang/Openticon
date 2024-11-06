package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.FavoritesEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<FavoritesEntity,Long> {

    Optional<FavoritesEntity> findByMemberIdAndEmoticonId(Long memberId,Long emoticonId);
}
