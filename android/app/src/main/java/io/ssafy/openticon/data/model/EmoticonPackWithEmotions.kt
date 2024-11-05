package io.ssafy.openticon.data.model

data class EmoticonPackWithEmotions(
    val emoticonPackEntity: EmoticonPackEntity,
    val emotions: List<Emoticon>
)
