package io.ssafy.openticon.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EmoticonPackPurchaseRequestDto {
    @JsonProperty("emoticon_pack_id")
    private Long emoticonPackId;
}
