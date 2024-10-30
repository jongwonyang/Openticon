package io.ssafy.openticon.domain.model

data class EmoticonPack(
    val id: Int,
    val title: String,
    val author: String,
    val thumbnail: String,
    val price: Int = 0,
    val description: String = ""
)