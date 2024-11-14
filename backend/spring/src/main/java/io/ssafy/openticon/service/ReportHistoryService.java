package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.request.ReportPackRequestDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.ReportHistoryEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.ReportHistoryRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ReportHistoryService {

    private final ReportHistoryRepository reportHistoryRepository;
    private final PackService packService;

    public ReportHistoryService(ReportHistoryRepository reportHistoryRepository, PackService packService) {
        this.reportHistoryRepository = reportHistoryRepository;
        this.packService = packService;
    }

    public void report(ReportPackRequestDto reportPackRequestDto, Long memberId) {
        EmoticonPackEntity emoticonPackEntity = packService.getUuid(reportPackRequestDto.getUuid());
        if(emoticonPackEntity == null){
            throw new IllegalArgumentException("해당하는 이모티콘 팩이 없음");
        }

        Long emoticonPackId = emoticonPackEntity.getId();

        if(reportHistoryRepository.findByMemberIdAndEmoticonPackIdAndDeletedAtIsNull(memberId,emoticonPackId).isPresent()){
            throw new OpenticonException(ErrorCode.DUPLICATE_REPORT);
        }


        ReportHistoryEntity reportHistoryEntity = ReportHistoryEntity.builder()
                .emoticonPackId(emoticonPackId)
                .memberId(memberId)
                .description(reportPackRequestDto.getDescription())
                .build();

        if(reportHistoryRepository.findByEmoticonPackIdAndDeletedAtIsNull(emoticonPackId).size()>=9){
            EmoticonPackEntity pack = packService.getPackById(reportHistoryEntity.getEmoticonPackId()).orElseThrow();
            pack.setBlacklist(true);
            packService.save(pack);
        }

        reportHistoryRepository.save(reportHistoryEntity);
    }
}
