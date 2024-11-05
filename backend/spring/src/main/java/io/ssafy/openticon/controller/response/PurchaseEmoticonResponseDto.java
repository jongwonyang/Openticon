package io.ssafy.openticon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
@Builder
public class PurchaseEmoticonResponseDto {
    private Long packId;

    private String packName;

    private String thumbnailImg;

    private Boolean isHide;

    private Boolean isPublic;

    private String listImage;
}
