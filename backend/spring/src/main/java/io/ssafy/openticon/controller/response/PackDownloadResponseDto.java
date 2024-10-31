package io.ssafy.openticon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class PackDownloadResponseDto {

    private String thumbnailImg;
    private String listImg;
    private List<String> emoticons;


}