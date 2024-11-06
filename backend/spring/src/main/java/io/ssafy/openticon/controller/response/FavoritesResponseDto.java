package io.ssafy.openticon.controller.response;

import lombok.Getter;

import java.util.List;

@Getter
public class FavoritesResponseDto {

    private List<String> emotions;

    public FavoritesResponseDto(){}

    public FavoritesResponseDto(List<String> emotions){
        this.emotions=emotions;
    }
}
