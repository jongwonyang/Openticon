package io.ssafy.openticon.ui.data.model

data class EditDeviceTokenRequestDto(
    val deviceToken: String, // 필수 필드로 nullable이 아니게 설정
    val mobile: Boolean      // 필수 필드로 nullable이 아니게 설정
)
