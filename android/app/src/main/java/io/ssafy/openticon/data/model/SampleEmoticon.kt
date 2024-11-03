package io.ssafy.openticon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class SampleEmoticon(
    val imageResource: Int,
    val imageUrl: String,
    val name : String = "Default"
)
