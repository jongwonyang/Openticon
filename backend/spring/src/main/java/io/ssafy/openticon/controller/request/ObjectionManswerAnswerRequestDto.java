package io.ssafy.openticon.controller.request;

import io.ssafy.openticon.dto.ReportStateType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ObjectionManswerAnswerRequestDto {
    private Long objectionId;
    private String content;
    private ReportStateType reportStateType;
}
