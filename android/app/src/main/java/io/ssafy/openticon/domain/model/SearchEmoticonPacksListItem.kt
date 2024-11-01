package io.ssafy.openticon.domain.model

import io.ssafy.openticon.data.model.EmoticonPackResponseDto
import io.ssafy.openticon.data.model.PageEmoticonPackResponseDto

data class SearchEmoticonPacksListItem(
    val id: Int,
    val title: String,
    val author: String,
    val thumbnail: String
)

fun EmoticonPackResponseDto.toSearchEmoticonPacksListItem(): SearchEmoticonPacksListItem {
    return SearchEmoticonPacksListItem(
        id = id,
        title = title,
        author = member.nickname,
        thumbnail = thumbnailImg
    )
}

fun PageEmoticonPackResponseDto.toSearchEmoticonPacksList(): Pair<List<SearchEmoticonPacksListItem>, Boolean> {
    return Pair(
        content.map { it.toSearchEmoticonPacksListItem() },
        last
    )
}