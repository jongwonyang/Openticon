package io.ssafy.openticon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class PurchaseEmoticonResponseDto {

    private String packName;

    private String thumbnailImg;

    private Boolean isHide;
}
