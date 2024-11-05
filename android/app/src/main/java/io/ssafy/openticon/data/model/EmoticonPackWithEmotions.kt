package io.ssafy.openticon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class EmoticonPackWithEmotions(
    val emoticonPackEntity: EmoticonPackEntity,
    val emotions: List<Emoticon>
)
