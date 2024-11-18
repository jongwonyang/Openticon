package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.ObjectionEntity;
import io.ssafy.openticon.entity.ObjectionSubmitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ObjectionSumbitRepository extends JpaRepository<ObjectionSubmitEntity, Long> {
    Optional<ObjectionSubmitEntity> findByObjectionEntity(ObjectionEntity objectionEntity);
}
