package io.ssafy.openticon.controller.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EditDeviceTokenRequestDto {
    @Column(nullable = false)
    private String deviceToken;
    @Column(nullable = false)
    private boolean mobile;
}
