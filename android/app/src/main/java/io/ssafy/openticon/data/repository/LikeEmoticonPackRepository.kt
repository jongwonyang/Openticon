package io.ssafy.openticon.data.repository

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.SampleEmoticonPack

class LikeEmoticonPackRepository {
    fun getLikeEmoticonPack(): SampleEmoticonPack {
        return SampleEmoticonPack("즐겨찾기", android.R.drawable.ic_menu_preferences, listOf(
                Emoticon(R.drawable.icon_9, ""),
                Emoticon(R.drawable.icon_28, ""),
            Emoticon(R.drawable._2ac25c78c76acdfffc908990700d7abf43ad912ad8dd55b04db6a64cddaf76d, ""),
            )
        )
    }
}