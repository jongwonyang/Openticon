package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.entity.AnswerEntity;
import io.ssafy.openticon.entity.ObjectionEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AnswerResponseDto {
    private Long id;
    private String content;
    private String createdAt;

    public AnswerResponseDto(AnswerEntity answerEntity){
        this.id = answerEntity.getId();
        this.content = answerEntity.getContent();
        this.createdAt = answerEntity.getCreatedAt().atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
