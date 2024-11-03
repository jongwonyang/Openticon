package io.ssafy.openticon.domain.usecase

import io.ssafy.openticon.R
import io.ssafy.openticon.data.model.SampleEmoticon
import io.ssafy.openticon.data.model.SampleEmoticonPack

class GetEmoticonPacksUseCase {
    operator fun invoke(): List<SampleEmoticonPack> {
        return listOf(
            SampleEmoticonPack("Pack1", R.drawable.main_img, listOf(
                SampleEmoticon(R.drawable.icon_9, ""), SampleEmoticon(R.drawable.icon_28, ""))),
        )
    }
}
