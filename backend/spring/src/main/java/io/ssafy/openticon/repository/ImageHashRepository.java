package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.ImageHashEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.awt.print.Pageable;
import java.util.List;

public interface ImageHashRepository extends JpaRepository<ImageHashEntity,Long> {
    @Query("SELECT i FROM ImageHashEntity i JOIN FETCH i.emoticonPackEntity e JOIN FETCH e.download d")
    List<ImageHashEntity> findAll(Sort sort);

}
