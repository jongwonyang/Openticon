package io.ssafy.openticon.ui.sample

import io.ssafy.openticon.domain.model.EmoticonPack

object EmoticonPackSampleData {
    val latestPacks = listOf(
        EmoticonPack(
            id = 1,
            title = "레니콘 슈퍼디럭스",
            author = "무치",
            thumbnail = "https://picsum.photos/200/200?r=aaa",
            description = "100가지맛으로 돌아온 쭈글레니콘 슈퍼디럭스에디션"
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

    val samplePack = EmoticonPack(
        id = 1,
        title = "레니콘 슈퍼디럭스",
        author = "무치",
        thumbnail = "https://picsum.photos/200/200?r=aaa",
        description = "100가지맛으로 돌아온 쭈글레니콘 슈퍼디럭스에디션"
    )

    val samplePackItems = listOf(
        "https://picsum.photos/200/200?r=1",
        "https://picsum.photos/200/200?r=2",
        "https://picsum.photos/200/200?r=3",
        "https://picsum.photos/200/200?r=4",
        "https://picsum.photos/200/200?r=5",
        "https://picsum.photos/200/200?r=6",
        "https://picsum.photos/200/200?r=7",
        "https://picsum.photos/200/200?r=8",
        "https://picsum.photos/200/200?r=9",
        "https://picsum.photos/200/200?r=10",
        "https://picsum.photos/200/200?r=11"
    )
}