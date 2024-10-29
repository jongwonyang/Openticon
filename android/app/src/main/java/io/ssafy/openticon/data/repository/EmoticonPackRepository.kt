package io.ssafy.openticon.data.repository

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack

class EmoticonPackRepository {
    // 데이터 소스에서 ImoticonPack을 가져오는 예시
    fun getImoticonPacks(): List<EmoticonPack> {
        return listOf(
            EmoticonPack("ExamplePack", R.drawable.main_img, listOf(Emoticon(R.drawable.icon_9, ""),Emoticon(R.drawable.icon_28, ""),Emoticon(R.drawable.icon_9, ""),Emoticon(R.drawable.icon_28, ""),Emoticon(R.drawable.icon_9, ""),Emoticon(R.drawable.icon_28, ""),Emoticon(R.drawable.icon_9, ""),Emoticon(R.drawable.icon_28, "")))
        )
    }
}
