package io.ssafy.openticon.service;

import io.ssafy.openticon.entity.PermissionEntity;
import io.ssafy.openticon.repository.PermissionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public List<Long> permissionUsers(Long packId){
        List<PermissionEntity> permissionEntities=permissionRepository.findByEmoticonPackId(packId);

        List<Long> accessedUsers=new ArrayList<>();
        for(PermissionEntity permissionEntity:permissionEntities){
            accessedUsers.add(permissionEntity.getMember().getId());
        }
        return accessedUsers;
    }
}
