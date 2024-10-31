package io.ssafy.openticon.entity;

import io.ssafy.openticon.dto.PermissionId;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "permission")
@Getter
@NoArgsConstructor
@IdClass(PermissionId.class)
public class PermissionEntity {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private MemberEntity member;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "emoticon_pack_id", nullable = false)
    private EmoticonPackEntity emoticonPack;


    public PermissionEntity(MemberEntity member, EmoticonPackEntity emoticonPack) {
        this.member = member;
        this.emoticonPack = emoticonPack;
    }
}
