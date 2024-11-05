package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.dto.Category;
import io.ssafy.openticon.entity.EmoticonPackEntity;
import lombok.Getter;

import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class ImageHashResponseDto {

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
    private String target;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss:SS");

    public ImageHashResponseDto(EmoticonPackEntity emoticonPackEntity,String target) {
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
        this.shareLink = emoticonPackEntity.getShareLink();

        // KST 변환 및 포맷 적용
        this.createdAt = formatToKST(emoticonPackEntity.getCreatedAt());
        this.updatedAt = formatToKST(emoticonPackEntity.getUpdatedAt());

        // 태그 이름 목록 설정
        this.tags = emoticonPackEntity.getTagLists().stream()
                .map(tagList -> tagList.getTag().getTagName())
                .collect(Collectors.toList());

        this.target=target;
    }

    private String formatToKST(OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(ZoneId.of("Asia/Seoul")).format(formatter);
    }
}
