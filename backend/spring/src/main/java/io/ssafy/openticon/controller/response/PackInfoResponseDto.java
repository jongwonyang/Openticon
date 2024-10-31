package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.dto.Category;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
public class PackInfoResponseDto {

    private String title;

    private String nickname;

    private boolean isAIGenerated;

    private int price;

    private long view;

    private Category category;

    private String thumbnailImg;

    private String listImg;

    private List<String> emoticons;

    private String description;

    private OffsetDateTime createdAt;

    public PackInfoResponseDto(EmoticonPackEntity emoticonPackEntity, List<String> emoticons){
        this.title=emoticonPackEntity.getTitle();
        this.nickname=emoticonPackEntity.getMember().getNickname();
        this.isAIGenerated=emoticonPackEntity.isAiGenerated();
        this.price=emoticonPackEntity.getPrice();
        this.view=emoticonPackEntity.getView();
        this.category=emoticonPackEntity.getCategory();
        this.thumbnailImg=emoticonPackEntity.getThumbnailImg();
        this.listImg=emoticonPackEntity.getListImg();
        this.description=emoticonPackEntity.getDescription();
        this.createdAt=emoticonPackEntity.getCreatedAt();
        this.emoticons=emoticons;
    }


}
