package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.Emoticon
import io.ssafy.openticon.data.model.EmoticonPack

class GetEmoticonPacksUseCase {
    operator fun invoke(): List<EmoticonPack> {
        return listOf(
            EmoticonPack("Pack1", R.drawable.main_img, listOf(
                Emoticon(R.drawable.icon_9, ""), Emoticon(R.drawable.icon_28, ""))),
        )
    }
}
