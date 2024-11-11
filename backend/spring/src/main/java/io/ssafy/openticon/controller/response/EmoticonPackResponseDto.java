package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.dto.Category;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import io.ssafy.openticon.entity.TagListEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.text.html.HTML;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
public class EmoticonPackResponseDto {
    private Long id;
    private String title;
    private PackMemberResponseDto member;
    private boolean isAiGenerated;
    private int price;
    private Long view;
    private boolean isPublic;
    private boolean isBlacklist;
    private Category category;
    private String thumbnailImg;
    private String listImg;
    private String description;
    private String shareLink;
    private String createdAt;
    private String updatedAt;
    private List<String> tags;
    private int download;
    private String deletedAt;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SS");

    public EmoticonPackResponseDto(EmoticonPackEntity emoticonPackEntity) {
        PackMemberResponseDto packMemberResponseDto = new PackMemberResponseDto();
        packMemberResponseDto.setId(emoticonPackEntity.getMember().getId());
        packMemberResponseDto.setEmail(emoticonPackEntity.getMember().getEmail());
        packMemberResponseDto.setNickname(emoticonPackEntity.getMember().getNickname());

        this.id = emoticonPackEntity.getId();
        this.title = emoticonPackEntity.getTitle();
        this.member = packMemberResponseDto;
        this.isAiGenerated = emoticonPackEntity.isAiGenerated();
        this.price = emoticonPackEntity.getPrice();
        this.view = emoticonPackEntity.getView();
        this.isPublic = emoticonPackEntity.isPublic();
        this.isBlacklist = emoticonPackEntity.getBlacklist();
        this.category = emoticonPackEntity.getCategory();
        this.thumbnailImg = emoticonPackEntity.getThumbnailImg();
        this.listImg = emoticonPackEntity.getListImg();
        this.description = emoticonPackEntity.getDescription();
        this.shareLink = emoticonPackEntity.getShareLink();
        this.download = emoticonPackEntity.getDownload();


        // KST 변환 및 포맷 적용
        this.createdAt = formatToKST(emoticonPackEntity.getCreatedAt());
        this.updatedAt = formatToKST(emoticonPackEntity.getUpdatedAt());
        if(emoticonPackEntity.getDeletedAt() != null){
            this.deletedAt = formatToKST(emoticonPackEntity.getUpdatedAt());
        }

        // 태그 이름 목록 설정
        this.tags = new ArrayList<>();
        for(TagListEntity tagListEntities : emoticonPackEntity.getTagLists()){
            tags.add(tagListEntities.getTag().getTagName());
        }
    }

    private String formatToKST(OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(ZoneId.of("Asia/Seoul")).format(formatter);
    }
}
