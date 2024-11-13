package io.ssafy.openticon.data.model

import io.ssafy.openticon.domain.model.EmoticonPack

data class PackDownloadResponseDto(
    val thumbnailImg: String,
    val listImg: String,
    val emoticons: List<String>,
    val id: Int,
    val title: String,
    val isPublic: Boolean,
    val uuid: String
) {
    fun toEmoticonPack(): EmoticonPack {
        return EmoticonPack(
            thumbnail = thumbnailImg,
            listImg = listImg,
            emoticonUrls = emoticons,
            id = id,
            title = title,
            isPublic = isPublic,
            uuid = uuid
        )
    }
}