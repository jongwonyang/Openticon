package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.EmoticonPackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PackRepository extends JpaRepository<EmoticonPackEntity,Long> {
    public EmoticonPackEntity findByShareLink(String uuid);
}
