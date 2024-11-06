package io.ssafy.openticon.service;

import io.ssafy.openticon.controller.request.ReportPackRequestDto;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.ReportHistoryEntity;
import io.ssafy.openticon.exception.ErrorCode;
import io.ssafy.openticon.exception.OpenticonException;
import io.ssafy.openticon.repository.ReportHistoryRepository;
import org.springframework.stereotype.Service;


@Service
public class ReportHistoryService {

    private final ReportHistoryRepository reportHistoryRepository;
    private final PackService packService;

    public ReportHistoryService(ReportHistoryRepository reportHistoryRepository, PackService packService) {
        this.reportHistoryRepository = reportHistoryRepository;
        this.packService = packService;
    }

    public void report(ReportPackRequestDto reportPackRequestDto, Long memberId) {

        if(reportHistoryRepository.findByMemberIdAndEmoticonPackIdAndDeletedAtIsNull(memberId,reportPackRequestDto.getEmoticonPackId()).isPresent()){
            throw new OpenticonException(ErrorCode.DUPLICATE_REPORT);
        }

        ReportHistoryEntity reportHistoryEntity = ReportHistoryEntity.builder()
                .emoticonPackId(reportPackRequestDto.getEmoticonPackId())
                .memberId(memberId)
                .description(reportPackRequestDto.getDescription())
                .build();

        if(reportHistoryRepository.findByEmoticonPackIdAndDeletedAtIsNull(reportPackRequestDto.getEmoticonPackId()).size()>=9){
            EmoticonPackEntity pack = packService.getPackById(reportHistoryEntity.getEmoticonPackId());
            pack.setBlacklist(true);
            packService.save(pack);
        }

        reportHistoryRepository.save(reportHistoryEntity);

    }
}
