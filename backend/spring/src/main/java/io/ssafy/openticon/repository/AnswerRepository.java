package io.ssafy.openticon.repository;

import io.ssafy.openticon.entity.AnswerEntity;
import io.ssafy.openticon.entity.ObjectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AnswerRepository extends JpaRepository<AnswerEntity, Long> {
    Optional<AnswerEntity> findByObjectionEntity(ObjectionEntity objectionEntity);
}
