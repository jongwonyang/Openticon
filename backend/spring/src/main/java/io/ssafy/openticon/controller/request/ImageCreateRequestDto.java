package io.ssafy.openticon.controller.request;

import lombok.Getter;

@Getter
public class ImageCreateRequestDto {

    private String prompt;

    public ImageCreateRequestDto(){}

    public ImageCreateRequestDto(String prompt){
        this.prompt=prompt;
    }
}
