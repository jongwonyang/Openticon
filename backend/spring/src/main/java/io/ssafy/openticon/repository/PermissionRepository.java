package io.ssafy.openticon.repository;

import io.ssafy.openticon.dto.PermissionId;
import io.ssafy.openticon.entity.PermissionEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PermissionRepository extends JpaRepository<PermissionEntity, PermissionId> {

    public List<PermissionEntity> findByEmoticonPackId(Long emoticonPackId);
}
