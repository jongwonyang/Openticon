package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.repository.ObjectionRepository;
import io.ssafy.openticon.repository.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ObjectionService {
    @Autowired
    ObjectionRepository objectionRepository;

    @Autowired
    PackRepository packRepository;

    public Page<EmoticonPackResponseDto> getObjectionList(MemberEntity member, Pageable pageable){
        return packRepository.findByMemberAndIsBlacklist(member, pageable).map(EmoticonPackResponseDto::new);
    }
}
