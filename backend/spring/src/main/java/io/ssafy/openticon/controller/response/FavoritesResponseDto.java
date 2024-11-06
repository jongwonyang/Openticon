package io.ssafy.openticon.controller.response;

import lombok.Getter;


@Getter
public class FavoritesResponseDto {

    private Long favoriteId;

    private String url;


    public FavoritesResponseDto(Long favoriteId, String url){
        this.favoriteId=favoriteId;
        this.url=url;
    }
}
