package io.ssafy.openticon.data.repository

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack

class LikeEmoticonPackRepository {
    fun getLikeEmoticonPack(): EmoticonPack {
        return EmoticonPack("즐겨찾기", android.R.drawable.ic_menu_preferences, listOf(
                Emoticon(R.drawable.icon_9, ""),
                Emoticon(R.drawable.icon_28, ""),
            )
        )
    }
}