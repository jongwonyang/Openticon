package io.ssafy.openticon.data.model

import io.ssafy.openticon.domain.model.EmoticonPack

data class PackInfoResponseDto(
    val id: Int,
    val title: String,
    val author: AuthorResponseDto,
    val price: Int,
    val view: Int,
    val category: String,
    val thumbnailImg: String,
    val listImg: String,
    val emoticons: List<String>,
    val description: String,
    val createdAt: String,
    val public: Boolean,
    val aigenerated: Boolean,
    val shareLink: String
)

fun PackInfoResponseDto.toEmoticonPack(): EmoticonPack {
    return EmoticonPack(
        id = id,
        title = title,
        thumbnail = thumbnailImg,
        listImg = listImg,
        isPublic = public,
        emoticonUrls = emoticons,
        uuid = shareLink
    )
}