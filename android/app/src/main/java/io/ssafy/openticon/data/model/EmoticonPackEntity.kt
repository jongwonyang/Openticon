package io.ssafy.openticon.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import io.ssafy.openticon.domain.model.EmoticonPack

@Entity(tableName = "emoticon_packs")
data class EmoticonPackEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val thumbnail: String,
    val listImg: String,
    val isPrivate: Boolean,
    val downloaded: Boolean
)

fun EmoticonPack.toEmoticonPackEntity(): EmoticonPackEntity {
    return EmoticonPackEntity(
        id = id,
        title = title,
        thumbnail = thumbnail,
        listImg = listImg,
        isPrivate = isPublic,
        downloaded = false
    )
}