package io.ssafy.openticon.repository;

import io.ssafy.openticon.dto.EmoticonPack;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackRepository extends JpaRepository<EmoticonPackEntity,Long> {
    public EmoticonPackEntity findByShareLink(String uuid);

    Page<EmoticonPackEntity> findByTitleContaining(String title, Pageable pageable);

//    Page<EmoticonPackEntity> findByTagContaining(String tag, Pageable pageable);

//    Page<EmoticonPackEntity> findByAuthorContaining(String author, Pageable pageable);
}
