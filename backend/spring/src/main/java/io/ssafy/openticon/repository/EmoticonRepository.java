package io.ssafy.openticon.repository;


import io.ssafy.openticon.entity.EmoticonEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmoticonRepository extends JpaRepository<EmoticonEntity,Long> {


}
