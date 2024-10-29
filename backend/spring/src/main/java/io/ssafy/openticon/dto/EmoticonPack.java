package io.ssafy.openticon.dto;

import io.ssafy.openticon.controller.request.EmoticonUploadRequestDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class EmoticonPack {

    private String packTitle;

    private boolean isAiGenerated;

    private boolean isPublic;

    private Category category;

    private String description;

    private int price;

    private List<String> tags;

    private MultipartFile thumbnailImg;

    private MultipartFile listImg;

    private List<MultipartFile> emoticons;

    public EmoticonPack(EmoticonUploadRequestDto emoticonUploadRequestDto){
        this.packTitle=emoticonUploadRequestDto.getPackTitle();
        this.isAiGenerated=emoticonUploadRequestDto.isAiGenerated();
        this.isPublic=emoticonUploadRequestDto.isPublic();
        this.category=emoticonUploadRequestDto.getCategory();
        this.description=emoticonUploadRequestDto.getDescription();
        this.price=emoticonUploadRequestDto.getPrice();
        this.tags=emoticonUploadRequestDto.getTags();
    }

    public void setImages(MultipartFile thumbnailImg, MultipartFile listImg, List<MultipartFile> emoticons){
        this.thumbnailImg=thumbnailImg;
        this.listImg=listImg;
        this.emoticons=emoticons;
    }
}
