package io.ssafy.openticon.repository;


import io.ssafy.openticon.entity.EmoticonEntity;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmoticonRepository extends JpaRepository<EmoticonEntity,Long> {

    List<EmoticonEntity> getEmoticonEntitiesByEmoticonPackId(Long packId);

    EmoticonEntity getEmoticonEntityByEmoticonPackAndEmoticonOrder(EmoticonPackEntity emoticonPackEntity, Integer order);
}
