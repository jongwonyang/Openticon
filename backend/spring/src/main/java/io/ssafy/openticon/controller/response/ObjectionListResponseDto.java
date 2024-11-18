package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.dto.ReportStateType;
import io.ssafy.openticon.dto.ReportType;
import io.ssafy.openticon.entity.ObjectionEntity;
import lombok.Getter;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Getter
public class ObjectionListResponseDto {
    private Long id;

    private PackInfoResponseDto emoticonPack;

    private String submitRequest;

    private ReportType type;

    private ReportStateType state;

    private String createdAt;

    private String completedAt;

    public ObjectionListResponseDto(ObjectionEntity objectionEntity, String submitRequest, List<String> emoticons) {
        this.id = objectionEntity.getId();
        this.emoticonPack = new PackInfoResponseDto(objectionEntity.getEmoticonPack(), emoticons);
        this.type = objectionEntity.getType();
        this.state = objectionEntity.getState();
        this.createdAt = objectionEntity.getCreatedAt().atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.completedAt = objectionEntity.getCompletedAt() != null ? objectionEntity.getCompletedAt().atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : "null";
        this.submitRequest = submitRequest;
    }
}
