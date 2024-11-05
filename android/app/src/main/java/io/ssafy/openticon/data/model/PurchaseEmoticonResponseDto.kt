package io.ssafy.openticon.data.model

import io.ssafy.openticon.domain.model.EmoticonPack

data class PurchaseEmoticonResponseDto(
    val packId: Int,
    val packName: String,
    val thumbnailImg: String,
    val isHide: Boolean,
    val isPublic: Boolean,
    val listImage: String
)

fun PurchaseEmoticonResponseDto.toEmoticonPack(): EmoticonPack {
    return EmoticonPack(
        id = packId,
        title = packName,
        thumbnail = thumbnailImg,
        listImg = listImage,
        isPublic = isPublic
    )
}