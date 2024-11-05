package io.ssafy.openticon.controller.response;

import io.ssafy.openticon.entity.MemberEntity;
import lombok.Getter;
import org.checkerframework.checker.units.qual.C;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Getter
public class MemberResponseDto {
    private Long id;
    private String email;
    private String nickname;
    private int point;
    private String createdAt;
    private String updatedAt;
    private boolean manager;
    private boolean isResigned;
    private String mobile_fcm;
    private String web_fcm;
    private String profile_image;

    public MemberResponseDto(MemberEntity member){
        this.id = member.getId();
        this.email = member.getEmail();
        this.nickname = member.getNickname();
        this.point = member.getPoint();
        this.createdAt = member.getCreatedAt().atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.updatedAt = member.getUpdatedAt().atZoneSameInstant(ZoneId.of("Asia/Seoul"))
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.manager = member.getManager();
        this.isResigned = member.getIsResigned();
        this.mobile_fcm = member.getMobile_fcm();
        this.web_fcm = member.getWeb_fcm();
        this.profile_image = member.getProfile_image();
    }
}
