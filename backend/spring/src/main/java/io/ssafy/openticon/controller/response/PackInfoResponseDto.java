package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.dto.Category;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.TagEntity;
import io.ssafy.openticon.entity.TagListEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
public class PackInfoResponseDto {

    private Long id;

    private String title;

    private AuthorResponseDto author;

    private boolean isAIGenerated;

    private int price;

    private long view;

    private boolean isPublic;

    private boolean isBlacklist;

    private Category category;

    private String thumbnailImg;

    private String listImg;

    private List<String> emoticons;

    private String description;

    private String createdAt;

    private List<String> tags;

    private String sharedLink;

    private int download;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SS");

    public PackInfoResponseDto(EmoticonPackEntity emoticonPackEntity, List<String> emoticons){
        this.id = emoticonPackEntity.getId();
        this.title=emoticonPackEntity.getTitle();
        this.author = new AuthorResponseDto(
                emoticonPackEntity.getMember().getId(),
                emoticonPackEntity.getMember().getNickname(),
                emoticonPackEntity.getMember().getProfile_image()
                );
        this.isAIGenerated=emoticonPackEntity.isAiGenerated();
        this.price=emoticonPackEntity.getPrice();
        this.view=emoticonPackEntity.getView();
        this.category=emoticonPackEntity.getCategory();
        this.thumbnailImg=emoticonPackEntity.getThumbnailImg();
        this.listImg=emoticonPackEntity.getListImg();
        this.description=emoticonPackEntity.getDescription();
        this.createdAt = formatToKST(emoticonPackEntity.getCreatedAt());
        this.emoticons=emoticons;
        this.isPublic = emoticonPackEntity.isPublic();
        this.isBlacklist = emoticonPackEntity.getBlacklist();
        this.tags = new ArrayList<>();
        for(TagListEntity tagListEntity : emoticonPackEntity.getTagLists()){
            tags.add(tagListEntity.getTag().getTagName());
        }
        this.sharedLink=emoticonPackEntity.getShareLink();
        this.download = emoticonPackEntity.getDownload();
    }

    private String formatToKST(OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(ZoneId.of("Asia/Seoul")).format(formatter);
    }
}
