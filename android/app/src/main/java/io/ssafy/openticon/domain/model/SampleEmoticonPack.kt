package io.ssafy.openticon.domain.model

data class SampleEmoticonPack(
    val id: Int,
    val title: String,
    val author: String,
    val thumbnail: String,
    val price: Int = 0,
    val description: String = ""
)