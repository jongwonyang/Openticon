package io.ssafy.openticon.data.model

data class PurchaseEmoticonResponseDto(
    val packId: Int,
    val packName: String,
    val thumbnailImg: String,
    val isHide: Boolean
)