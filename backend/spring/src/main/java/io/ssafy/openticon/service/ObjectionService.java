package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.request.ObjectionTestRequestDto;
import io.ssafy.openticon.controller.response.EmoticonPackResponseDto;
import io.ssafy.openticon.controller.response.ObjectionListResponseDto;
import io.ssafy.openticon.dto.ReportStateType;
import io.ssafy.openticon.dto.ReportType;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.MemberEntity;
import io.ssafy.openticon.entity.ObjectionEntity;
import io.ssafy.openticon.repository.ObjectionRepository;
import io.ssafy.openticon.repository.PackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.Option;
import java.util.EventObject;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ObjectionService {
    @Autowired
    ObjectionRepository objectionRepository;

    @Autowired
    PackRepository packRepository;

    public Page<ObjectionListResponseDto> getObjectionList(MemberEntity member, Pageable pageable){
        return objectionRepository.findByMember(member, pageable).map(ObjectionListResponseDto::new);
    }

    // 이의 제기 추가(테스트 용)
    @Transactional
    public String testSubmitObjection(MemberEntity member, ObjectionTestRequestDto requestDto){
        EmoticonPackEntity emoticonPackEntity = packRepository.findById(requestDto.getEmoticonPackId())
                .orElseThrow(() -> new NoSuchElementException("이모티콘 팩을 찾을 수 없습니다."));

        // 블랙리스트인가?
        if(emoticonPackEntity.getBlacklist()){
            throw new NoSuchElementException("이미 이모티콘 팩이 차단되어 있습니다.");
        }

        // 소유자인가?
        if(!emoticonPackEntity.getMember().getEmail().equals(member.getEmail())){
            throw new NoSuchElementException("해당 이모티콘 팩의 작가가 아닙니다.");
        }

        emoticonPackEntity.setBlacklist(true);
        packRepository.save(emoticonPackEntity);
        objectionEmoticonPack(emoticonPackEntity, requestDto.getReportType());
        return "success";
    }

    // 이모티콘 팩이 블랙리스트로 만들어야 하는 순간에 불러오는 서비스
    @Transactional
    public void objectionEmoticonPack(EmoticonPackEntity emoticonPackEntity, ReportType reportType){
        ObjectionEntity objectionEntity = ObjectionEntity.builder()
                .emoticonPack(emoticonPackEntity)
                .member(emoticonPackEntity.getMember())
                .type(reportType)
                .state(ReportStateType.PENDING)
                .build();

        objectionRepository.save(objectionEntity);
    }
}
