package io.ssafy.openticon.ui.sample

import io.ssafy.openticon.domain.model.EmoticonPack

object EmoticonPackSampleData {
    val latestPacks = (1..50).map { id ->
        EmoticonPack(
            id = id,
            title = "레니콘 슈퍼디럭스 $id",
            author = "무치 $id",
            thumbnail = "https://picsum.photos/200/200?r=$id",
            description = "100가지맛으로 돌아온 쭈글레니콘 슈퍼디럭스에디션 $id"
        )
    }

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