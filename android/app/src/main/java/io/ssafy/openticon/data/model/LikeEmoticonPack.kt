package io.ssafy.openticon.data.model

import kotlinx.serialization.Serializable

@Serializable
data class LikeEmoticonPack(
    val name: String = "LikePack",
    val filePath: Int,
    var emoticons: List<LikeEmoticon>,
)
