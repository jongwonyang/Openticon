package io.ssafy.openticon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class PurchaseHistoryResponseDto {
    private boolean isPurchase;
    private Long emoticonPackId;
    private String message;
}
