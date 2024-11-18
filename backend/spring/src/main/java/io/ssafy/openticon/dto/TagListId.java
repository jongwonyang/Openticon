package io.ssafy.openticon.dto;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TagListId implements Serializable {

    @Column(name = "emoticon_pack_id")
    private Long emoticonPackId;

    @Column(name = "tag_id")
    private Long tagId;

    // Default constructor
    public TagListId() {}

    // Getters and Setters

    // hashCode and equals methods
    @Override
    public int hashCode() {
        return Objects.hash(emoticonPackId, tagId);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TagListId)) return false;
        TagListId that = (TagListId) obj;
        return Objects.equals(emoticonPackId, that.emoticonPackId) &&
                Objects.equals(tagId, that.tagId);
    }

    // Constructors, Getters, and Setters omitted for brevity
}
