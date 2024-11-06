package io.ssafy.openticon.controller.response;

import lombok.Getter;

import java.util.List;

@Getter
public class FavoritesResponseDto {

    private Long emoticonId;

    private String url;

    public FavoritesResponseDto(){}

    public FavoritesResponseDto(Long emoticonId, String url){
        this.emoticonId=emoticonId;
        this.url=url;
    }
}
