package io.ssafy.openticon.data.repository

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack

class LikeEmoticonPackRepository {
    fun getLikeEmoticonPack(): EmoticonPack {
        return EmoticonPack("즐겨찾기", R.drawable.main_img, listOf(
                Emoticon(R.drawable.icon_9, ""),
                Emoticon(R.drawable.icon_28, ""),
            )
        )
    }
}