package io.ssafy.openticon.domain.model

import io.ssafy.openticon.data.model.PackInfoResponseDto

data class EmoticonPackDetail(
    val id: Int,
    val thumbnail: String,
    val title: String,
    val price: Int,
    val description: String,
    val items: List<String>,
    val createdAt: String,
    val authorId: Int,
    val authorNickname: String,
    val authorProfilePic: String
)

fun PackInfoResponseDto.toEmoticonPackDetail(): EmoticonPackDetail {
    return EmoticonPackDetail(
        id = id,
        thumbnail = thumbnailImg,
        title = title,
        price = price,
        description = description,
        items = emoticons,
        createdAt = createdAt,
        authorId = author.id,
        authorNickname = author.nickname,
        authorProfilePic = author.profile,
    )
}