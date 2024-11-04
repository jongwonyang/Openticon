package io.ssafy.openticon.data.model

import com.google.gson.annotations.SerializedName

data class PurchaseEmoticonPackRequestDto(
    @SerializedName("emoticon_pack_id") val emoticonPackId: Int
)
