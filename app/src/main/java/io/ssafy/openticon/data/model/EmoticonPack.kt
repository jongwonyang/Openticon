package io.ssafy.openticon.data.model

import kotlinx.serialization.Serializable


@Serializable
data class EmoticonPack(
    val name: String,
    val mainImageResource: Int,
    val images: List<Emoticon> , // Imoticon 객체 리스트로 구성
    val isPublic: Boolean = true,
    val isVisible: Boolean = true
)
