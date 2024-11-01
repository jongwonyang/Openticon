package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.dto.Category;
import io.ssafy.openticon.dto.ExamineType;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
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
    private ExamineType examine;
    private String shareLink;
    private String createdAt;
    private String updatedAt;

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
        this.isBlacklist = emoticonPackEntity.isBlacklist();
        this.category = emoticonPackEntity.getCategory();
        this.thumbnailImg = emoticonPackEntity.getThumbnailImg();
        this.listImg = emoticonPackEntity.getListImg();
        this.description = emoticonPackEntity.getDescription();
        this.examine = emoticonPackEntity.getExamine();
        this.shareLink = emoticonPackEntity.getShareLink();

        // KST 변환 및 포맷 적용
        this.createdAt = formatToKST(emoticonPackEntity.getCreatedAt());
        this.updatedAt = formatToKST(emoticonPackEntity.getUpdatedAt());
    }

    private String formatToKST(OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(ZoneId.of("Asia/Seoul")).format(formatter);
    }
}
