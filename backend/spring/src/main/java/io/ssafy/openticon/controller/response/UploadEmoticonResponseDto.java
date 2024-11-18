package io.ssafy.openticon.controller.response;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UploadEmoticonResponseDto {

    private String url;

    public UploadEmoticonResponseDto(String url) {
        this.url=url;
    }
}
