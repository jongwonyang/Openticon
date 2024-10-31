package io.ssafy.openticon.ui.sample

import io.ssafy.openticon.domain.model.EmoticonPack

object EmoticonPackSampleData {
    val latestPacks = listOf(
        EmoticonPack(
            id = 1,
            title = "레니콘 슈퍼디럭스",
            author = "무치",
            thumbnail = "https://picsum.photos/200/200?r=aaa"
        ),
        EmoticonPack(
            id = 2,
            title = "쭐어콘 오피셜2",
            author = "ㅉㄹ",
            thumbnail = "https://picsum.photos/200/200?r=bbb"
        ),
        EmoticonPack(
            id = 3,
            title = "고양이 나나",
            author = "송모찌",
            thumbnail = "https://picsum.photos/200/200?r=ccc"
        ),
        EmoticonPack(
            id = 4,
            title = "찌그러진 핑키빈콘",
            author = "콜렉터",
            thumbnail = "https://picsum.photos/200/200?r=ddd"
        )
    )
}