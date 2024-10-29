package io.ssafy.openticon.controller.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PointRequestDto {
    @JsonProperty("imp_uid")
    private String impUid;

    @JsonProperty("emoticon_pack_id")
    private Long emoticonPackId;

    private Long point;
}
