package io.ssafy.openticon.controller.response;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PackMemberResponseDto {
    private Long id;
    private String email;  // 이메일
    private String nickname;  // 닉네임 (유니크)
}
