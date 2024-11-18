package io.ssafy.openticon.data.model

import kotlinx.serialization.Serializable


@Serializable
data class SampleEmoticonPack(
    val name: String,
    val mainImageResource: Int,
    var images: List<SampleEmoticon>, // Imoticon 객체 리스트로 구성
    val isPublic: Boolean = true,
    val isVisible: Boolean = true
)
