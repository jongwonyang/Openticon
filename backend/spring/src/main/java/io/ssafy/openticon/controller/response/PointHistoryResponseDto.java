package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.dto.PointType;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
public class PointHistoryResponseDto {
    private Long id;
    private PointType type;
    private int point;
    private String createdAt;

    // 생성자
    public PointHistoryResponseDto(Long id, PointType type, int point, OffsetDateTime createdAt) {
        this.id = id;
        this.type = type;
        this.point = point;
        this.createdAt = createdAt.atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
