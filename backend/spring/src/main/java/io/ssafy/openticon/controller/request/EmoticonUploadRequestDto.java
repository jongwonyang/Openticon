package io.ssafy.openticon.controller.request;

import io.ssafy.openticon.dto.Category;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
public class EmoticonUploadRequestDto {

    private String packTitle;

    private Boolean isAiGenerated;

    private Boolean isPublic;

    private Category category;

    private String description;

    private int price;

    private List<String> tags;

    private MultipartFile thumbnailImg;

    private MultipartFile listImg;

    //private List<MultipartFile> emoticons;
}
