package io.ssafy.openticon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class Emoticon(
    val imageResource: Int,
    val imageUrl: String,
    val name : String = "Default"
)
