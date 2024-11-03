package io.ssafy.openticon.data.repository

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.SampleEmoticon
import io.ssafy.openticon.data.model.SampleEmoticonPack

class LikeEmoticonPackRepository {
    fun getLikeEmoticonPack(): SampleEmoticonPack {
        return SampleEmoticonPack("즐겨찾기", android.R.drawable.ic_menu_preferences, listOf(
                SampleEmoticon(R.drawable.icon_9, ""),
                SampleEmoticon(R.drawable.icon_28, ""),
            SampleEmoticon(R.drawable._2ac25c78c76acdfffc908990700d7abf43ad912ad8dd55b04db6a64cddaf76d, ""),
            )
        )
    }
}