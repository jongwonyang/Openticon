package io.ssafy.openticon.entity;

import io.ssafy.openticon.dto.TagListId;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "TagList")
public class TagListEntity {

    @EmbeddedId
    @Builder.Default
    private TagListId id = new TagListId();

    @ManyToOne
    @MapsId("emoticonPackId")
    @JoinColumn(name = "emoticon_pack_id")
    private EmoticonPackEntity emoticonPack;

    @ManyToOne
    @MapsId("tagId")
    @JoinColumn(name = "tag_id")
    private TagEntity tag;

    // Getters and Setters

    // Constructors, Getters, and Setters omitted for brevity
}
