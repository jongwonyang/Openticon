package io.ssafy.openticon.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class AuthorResponseDto {
    private Long id;
    private String nickname;
    private String profile;
}
