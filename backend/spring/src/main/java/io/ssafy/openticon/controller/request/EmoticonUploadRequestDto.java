package io.ssafy.openticon.controller.request;

import io.ssafy.openticon.dto.Category;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Getter
@Setter
@Schema(description = "이모티콘 팩 정보")
public class EmoticonUploadRequestDto {

    private String packTitle;

    private Boolean isAiGenerated;

    private Boolean isPublic;

    private Category category;

    private String description;

    private int price;

    private List<String> tags;
}
