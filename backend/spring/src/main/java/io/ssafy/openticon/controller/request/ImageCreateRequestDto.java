package io.ssafy.openticon.controller.request;

import lombok.Getter;

@Getter
public class ImageCreateRequestDto {

    private String prompt;

    private int seed;

    public ImageCreateRequestDto(){}

    public ImageCreateRequestDto(String prompt, int seed){
        this.prompt=prompt;
        this.seed=seed;
    }
}
