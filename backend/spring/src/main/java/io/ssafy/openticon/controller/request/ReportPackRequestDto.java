package io.ssafy.openticon.controller.request;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ReportPackRequestDto {

    private Long emoticonPackId;
    private String description;

}
