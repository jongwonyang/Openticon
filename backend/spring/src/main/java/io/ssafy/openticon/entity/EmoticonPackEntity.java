package io.ssafy.openticon.entity;

import io.ssafy.openticon.dto.Category;
import io.ssafy.openticon.dto.EmoticonPack;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hamcrest.core.Is;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "emoticon_pack")
public class EmoticonPackEntity {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)  // 지연 로딩 설정
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Column(name = "is_ai_generated", nullable = false)
    private boolean isAiGenerated = false;

    @Column(name = "price", nullable = false)
    private int price = 0;

    @Column(name = "view", nullable = false)
    private Long view = 0L;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = true;

    @Column(name = "is_blacklist", nullable = false)
    private boolean isBlacklist = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private Category category;

    @Column(name = "thumbnail_img")
    private String thumbnailImg;

    @Column(name = "list_img")
    private String listImg;

    @Column(name = "description")
    private String description;

    @Column(name = "share_link", nullable = false)
    private String shareLink="public";

    @OneToMany(mappedBy = "emoticonPack")
    private List<TagListEntity> tagLists;

    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private OffsetDateTime updatedAt;

    @Column(name = "deleted_at")
    private OffsetDateTime deletedAt;

    @Column(name = "download", nullable = false)
    private int download;


    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();;
    }

    @PrePersist
    public void prePersist() {
        this.createdAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();
        this.updatedAt = LocalDateTime.now().atZone(ZoneId.of("Asia/Seoul")).toOffsetDateTime();
        this.shareLink= UUID.randomUUID().toString();
        this.download = 0;
    }

    public EmoticonPackEntity(EmoticonPack emoticonPack, MemberEntity member, String thumbnailImg, String listImg){
        this.title=emoticonPack.getPackTitle();
        this.isAiGenerated=emoticonPack.getIsAiGenerated();
        this.isPublic=emoticonPack.getIsPublic();
        this.category=emoticonPack.getCategory();
        this.description=emoticonPack.getDescription();
        this.price=emoticonPack.getPrice();
        this.member=member;
        this.thumbnailImg=thumbnailImg;
        this.listImg=listImg;
    }

    public boolean getBlacklist() {
        return this.isBlacklist;
    }

    public void setBlacklist(boolean blacklist) {
        isBlacklist = blacklist;
    }

}
