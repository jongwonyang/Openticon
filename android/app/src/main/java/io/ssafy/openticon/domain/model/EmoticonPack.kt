package io.ssafy.openticon.domain.model

data class EmoticonPack(
    val id: Int,
    val title: String,
    val thumbnail: String,
    val listImg: String,
    val isPublic: Boolean,
    val emoticonUrls: List<String>
)
