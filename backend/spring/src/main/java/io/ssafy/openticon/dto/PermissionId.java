package io.ssafy.openticon.dto;

import lombok.EqualsAndHashCode;

import java.io.Serializable;

@EqualsAndHashCode
public class PermissionId implements Serializable {

    private Long member;
    private Long emoticonPack;

    public PermissionId() {}

    public PermissionId(Long member, Long emoticonPack) {
        this.member = member;
        this.emoticonPack = emoticonPack;
    }
}
