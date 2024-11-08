package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.request.ObjectionManswerAnswerRequestDto;
import io.ssafy.openticon.controller.request.ObjectionSubmitRequestDto;
import io.ssafy.openticon.controller.request.ObjectionTestRequestDto;
import io.ssafy.openticon.controller.response.AnswerResponseDto;
import io.ssafy.openticon.controller.response.ObjectionListResponseDto;
import io.ssafy.openticon.controller.response.ObjectionMsgResponseDto;
import io.ssafy.openticon.dto.ReportStateType;
import io.ssafy.openticon.dto.ReportType;
import io.ssafy.openticon.entity.*;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.AnswerRepository;
import io.ssafy.openticon.repository.ObjectionRepository;
import io.ssafy.openticon.repository.ObjectionSumbitRepository;
import io.ssafy.openticon.repository.PackRepository;
import org.apache.coyote.BadRequestException;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpClientErrorException;


import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ObjectionService {
    ObjectionRepository objectionRepository;
    PackRepository packRepository;
    ObjectionSumbitRepository objectionSumbitRepository;
    AnswerRepository answerRepository;

    public ObjectionService(ObjectionRepository objectionRepository, PackRepository packRepository, ObjectionSumbitRepository objectionSumbitRepository, AnswerRepository answerRepository){
        this.objectionRepository = objectionRepository;
        this.packRepository = packRepository;
        this.objectionSumbitRepository = objectionSumbitRepository;
        this.answerRepository = answerRepository;
    }


    public Page<ObjectionListResponseDto> getObjectionList(MemberEntity member, Pageable pageable){
        return objectionRepository.findByMember(member, pageable).map(ObjectionListResponseDto::new);
    }


    // 이의 제기 신청
    @Transactional
    public ObjectionMsgResponseDto submitObjection(MemberEntity member, ObjectionSubmitRequestDto request){
        ObjectionEntity objectionEntity = objectionRepository.findById(request.getObjectionId())
                .orElseThrow(()-> new OpenticonException(ErrorCode.NOT_FOUND_OBJECTION));

        if(!objectionEntity.getMember().getEmail().equals(member.getEmail())){
            throw new OpenticonException(ErrorCode.ACCESS_DENIED_OBJECTION);
        }

        if(objectionSumbitRepository.findByObjectionEntity(objectionEntity).isPresent()){
            throw new OpenticonException(ErrorCode.DUPLICATE_OBJECTION);
        }

        // 이의 신청 테이블에 저장
        ObjectionSubmitEntity objectionSubmitEntity = ObjectionSubmitEntity.builder()
                .objectionEntity(objectionEntity)
                .content(request.getContent())
                .build();
        objectionSumbitRepository.save(objectionSubmitEntity);

        // 상태를 접수 완료로 변경
        objectionEntity.setState(ReportStateType.RECEIVED);
        objectionRepository.save(objectionEntity);
        return ObjectionMsgResponseDto.builder()
                .code("OK")
                .message("이의 신청이 접수되었습니다.")
                .build();
    }


    // 이의 제기 추가(테스트 용)
    @Transactional
    public ObjectionMsgResponseDto testSubmitObjection(MemberEntity member, ObjectionTestRequestDto requestDto){
        EmoticonPackEntity emoticonPackEntity = packRepository.findById(requestDto.getEmoticonPackId())
                .orElseThrow(() -> new OpenticonException(ErrorCode.NOT_FOUND_OBJECTION));

        // 블랙리스트인가?
        if(emoticonPackEntity.getBlacklist()){
            throw new OpenticonException(ErrorCode.BLACKLIST_PACK);
        }

        emoticonPackEntity.setBlacklist(true);
        packRepository.save(emoticonPackEntity);
        objectionEmoticonPack(emoticonPackEntity, requestDto.getReportType());
        return ObjectionMsgResponseDto.builder()
                .code("OK")
                .message(emoticonPackEntity.getTitle()+" 이모티콘 팩을 차단하였습니다.")
                .build();
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

    // 이의제기에 대한 심사 결과를 보여줍니다.
    public AnswerResponseDto answerObjection(MemberEntity member, Long objectionId){
        ObjectionEntity objectionEntity = objectionRepository.findById(objectionId)
                .orElseThrow(() -> new OpenticonException(ErrorCode.NOT_FOUND_OBJECTION));

        if(!objectionEntity.getMember().getEmail().equals(member.getEmail())){
            throw new OpenticonException(ErrorCode.ACCESS_DENIED_OBJECTION);
        }

        AnswerEntity answerEntity = answerRepository.findByObjectionEntity(objectionEntity)
                .orElseThrow(() -> new OpenticonException(ErrorCode.NOT_FOUND_ANSWER));

        return new AnswerResponseDto(answerEntity);
    }


    // 관리자 리스트
    public Page<ObjectionListResponseDto> managerObjection(MemberEntity member, Pageable pageable){
        if(!member.getManager()){
            throw new OpenticonException(ErrorCode.ACCESS_DENIED);
        }
        return objectionRepository.findByState(ReportStateType.RECEIVED, pageable).map(ObjectionListResponseDto::new);
    }

    // 관리자 - 이의 제기에 대해 관리자가 심사
    @Transactional
    public ObjectionMsgResponseDto managerAnswerObjection(MemberEntity member, ObjectionManswerAnswerRequestDto request){
        if(!member.getManager()){
            throw new OpenticonException(ErrorCode.ACCESS_DENIED);
        }
        ObjectionEntity objectionEntity = objectionRepository.findById(request.getObjectionId())
                .orElseThrow(() -> new OpenticonException(ErrorCode.NOT_FOUND_OBJECTION));

        AnswerEntity answerEntity = AnswerEntity.builder()
                .content(request.getContent())
                .objectionEntity(objectionEntity)
                .build();

        if(request.getReportStateType().equals(ReportStateType.APPROVED)){
            // 사용자의 이의제기를 받아드림
            objectionEntity.setState(ReportStateType.APPROVED);
            EmoticonPackEntity emoticonPack = objectionEntity.getEmoticonPack();
            emoticonPack.setBlacklist(false); // 이의 제기를 받아들여서 차단을 해제함.
            packRepository.save(emoticonPack);
        }else if(request.getReportStateType().equals(ReportStateType.REJECTED)){
            // 사용자의 이의제기를 받아들이지 않음
            objectionEntity.setState(ReportStateType.REJECTED);
        }else{
            throw new OpenticonException(ErrorCode.OBJECTION_REPORT_STATE_TYPE_ERROR);
        }
        objectionRepository.save(objectionEntity);
        answerRepository.save(answerEntity);
        return ObjectionMsgResponseDto.builder()
                .code("OK")
                .message("SUCCESS")
                .build();
    }
}
